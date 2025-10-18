import cv2
import numpy as np

def crop_face(frame, bbox):
    h, w, _ = frame.shape
    x, y, w_box, h_box = bbox
    x, y = max(0, x), max(0, y)
    w_box, h_box = min(w - x, w_box), min(h - y, h_box)
    face_crop = frame[y:y+h_box, x:x+w_box]
    return face_crop

def preprocess_face(face_img):
    face_gray = cv2.cvtColor(face_img, cv2.COLOR_BGR2GRAY)
    face_resized = cv2.resize(face_gray, (48, 48))
    face_norm = face_resized / 255.0  
    face_input = np.expand_dims(face_norm, axis=(0, -1)).astype(np.float32)  # (1, 48, 48, 1)
    return face_input

def predict_emotion(interpreter, input_details, output_details, frame, bbox):
    face_crop = crop_face(frame, bbox)
    face_input = preprocess_face(face_crop)

    interpreter.set_tensor(input_details[0]['index'], face_input)
    interpreter.invoke()
    output_data = interpreter.get_tensor(output_details[0]['index'])
    emotion = np.argmax(output_data)
    return emotion