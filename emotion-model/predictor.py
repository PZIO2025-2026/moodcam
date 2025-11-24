"""Emotion Prediction Pipeline

This module provides image preprocessing and emotion prediction functions
for the TensorFlow Lite emotion recognition model.

Pipeline:
1. Crop face from frame using bounding box
2. Convert to grayscale
3. Resize to 48x48 pixels
4. Normalize pixel values to [0, 1]
5. Run TFLite model inference
6. Return predicted emotion label
"""

import cv2
import numpy as np


def crop_face(frame, bbox):
    """Crop face region from frame using bounding box.
    
    Applies boundary checks to ensure cropping stays within frame bounds.
    
    Args:
        frame (np.ndarray): Input BGR image frame
        bbox (tuple): Bounding box as (x, y, width, height)
        
    Returns:
        np.ndarray: Cropped face region
        
    Example:
        >>> face = crop_face(frame, (100, 100, 150, 150))
    """
    h, w, _ = frame.shape
    x, y, w_box, h_box = bbox
    x, y = max(0, x), max(0, y)
    w_box, h_box = min(w - x, w_box), min(h - y, h_box)
    face_crop = frame[y:y+h_box, x:x+w_box]
    return face_crop


def preprocess_face(face_img):
    """Preprocess face image for model input.
    
    Processing steps:
    1. Convert BGR to grayscale
    2. Resize to 48x48 pixels
    3. Normalize to [0, 1] range (divide by 255)
    4. Add batch and channel dimensions: (1, 48, 48, 1)
    
    Args:
        face_img (np.ndarray): BGR face image (any size)
        
    Returns:
        np.ndarray: Preprocessed face tensor of shape (1, 48, 48, 1)
        
    Note:
        Preprocessing must match the training pipeline exactly:
        face_norm = face_resized / 255.0
    """
    face_gray = cv2.cvtColor(face_img, cv2.COLOR_BGR2GRAY)
    face_resized = cv2.resize(face_gray, (48, 48))
    face_norm = face_resized / 255.0  
    face_input = np.expand_dims(face_norm, axis=(0, -1)).astype(np.float32)  # (1, 48, 48, 1)
    return face_input


def predict_emotion(interpreter, input_details, output_details, frame, bbox):
    """Predict emotion from face in frame.
    
    Complete prediction pipeline:
    1. Crop face using bounding box
    2. Preprocess face image
    3. Run TFLite model inference
    4. Return emotion with highest confidence
    
    Args:
        interpreter (tf.lite.Interpreter): Loaded TFLite interpreter
        input_details (list): Model input tensor details
        output_details (list): Model output tensor details
        frame (np.ndarray): Input BGR video frame
        bbox (tuple): Face bounding box (x, y, width, height)
        
    Returns:
        int: Predicted emotion label (0-6)
        
    Example:
        >>> emotion = predict_emotion(interpreter, input_details, 
        ...                          output_details, frame, (100, 100, 150, 150))
        >>> print(f"Emotion: {emotion}")  # Emotion: 3 (Happy)
    """
    face_crop = crop_face(frame, bbox)
    face_input = preprocess_face(face_crop)

    interpreter.set_tensor(input_details[0]['index'], face_input)
    interpreter.invoke()
    output_data = interpreter.get_tensor(output_details[0]['index'])
    emotion = np.argmax(output_data)
    return emotion