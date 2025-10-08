import os
import io
import numpy as np

try:
    import cv2  # type: ignore
except Exception:  # pragma: no cover
    cv2 = None


def read_image_unicode(path: str) -> np.ndarray:
    with open(path, 'rb') as f:
        data = f.read()
    image_array = np.frombuffer(data, dtype=np.uint8)
    image = cv2.imdecode(image_array, cv2.IMREAD_COLOR)
    if image is None:
        raise RuntimeError(f"Failed to decode image: {path}")
    return image


def write_image_unicode(path: str, image: np.ndarray) -> None:
    ext = os.path.splitext(path)[1].lower()
    if ext in ('.jpg', '.jpeg'):
        success, buf = cv2.imencode('.jpg', image, [int(cv2.IMWRITE_JPEG_QUALITY), 95])
    elif ext == '.png':
        success, buf = cv2.imencode('.png', image, [int(cv2.IMWRITE_PNG_COMPRESSION), 3])
    else:
        success, buf = cv2.imencode('.png', image, [int(cv2.IMWRITE_PNG_COMPRESSION), 3])
    if not success:
        raise RuntimeError(f"Failed to encode image for saving: {path}")
    with open(path, 'wb') as f:
        f.write(buf.tobytes())


def clamp(v: int, lo: int, hi: int) -> int:
    return max(lo, min(hi, v))


def build_source_from_neighbors(img: np.ndarray, top: int, bottom: int, left_band: tuple[int, int], right_band: tuple[int, int], target_width: int) -> np.ndarray:
    h = bottom - top
    l0, l1 = left_band
    r0, r1 = right_band
    slices: list[np.ndarray] = []
    if l1 > l0:
        left_patch = img[top:bottom, l0:l1]
        if left_patch.size:
            slices.append(left_patch)
    if r1 > r0:
        right_patch = img[top:bottom, r0:r1]
        if right_patch.size:
            right_patch = cv2.flip(right_patch, 1)
            slices.append(right_patch)
    if not slices:
        # fallback to a central band if neighbors are invalid
        w = img.shape[1]
        c0 = clamp(int(w * 0.25), 0, w - 1)
        c1 = clamp(int(w * 0.45), c0 + 1, w)
        slices = [img[top:bottom, c0:c1]]
    combined = np.concatenate(slices, axis=1)
    # Tile horizontally to reach target width
    reps = max(1, int(np.ceil(target_width / combined.shape[1])))
    tiled = np.tile(combined, (1, reps, 1))
    src = tiled[:, :target_width]
    return src


def fill_bottom_center(input_path: str, output_path: str) -> None:
    if cv2 is None:
        raise RuntimeError("OpenCV is required. Please install opencv-python-headless.")

    img = read_image_unicode(input_path)
    h, w = img.shape[:2]

    # Define target (empty grass) region near bottom-center
    top = clamp(int(h * 0.75), 0, h - 2)
    bottom = clamp(int(h * 0.98), top + 1, h)
    left = clamp(int(w * 0.35), 0, w - 2)
    right = clamp(int(w * 0.65), left + 1, w)

    # Neighbor bands to sample texture/objects from
    left_band = (clamp(int(w * 0.15), 0, w - 1), clamp(int(w * 0.35), 1, w))
    right_band = (clamp(int(w * 0.65), 0, w - 1), clamp(int(w * 0.85), 1, w))

    target_width = right - left
    src = build_source_from_neighbors(img, top, bottom, left_band, right_band, target_width)

    # Create mask for seamless cloning
    mask = np.full((bottom - top, right - left), 255, dtype=np.uint8)

    # Clone center point of the target region
    center = ((left + right) // 2, (top + bottom) // 2)

    # Prepare destination by placing src into a canvas to clone from
    # seamlessClone requires source the same size as mask
    try:
        mixed = cv2.seamlessClone(src, img, mask, center, cv2.MIXED_CLONE)
    except cv2.error:
        # Fallback to normal paste if seamlessClone fails
        mixed = img.copy()
        mixed[top:bottom, left:right] = src

    write_image_unicode(output_path, mixed)


if __name__ == "__main__":
    in_path = "/workspace/сцена 4.png"
    out_path = "/workspace/сцена 4_filled.png"
    if not os.path.exists(in_path):
        raise SystemExit(f"Input image not found: {in_path}")
    fill_bottom_center(in_path, out_path)
    print(f"Saved: {out_path}")

