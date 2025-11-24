"""Real-Time Emotion Detection Demo

This script demonstrates real-time emotion detection from webcam using:
- MediaPipe for face detection
- TensorFlow Lite for emotion classification
- OpenCV for video capture and display

Features:
- Detects faces in real-time using MediaPipe
- Predicts emotions every 10 frames (optimization)
- Displays bounding box and emotion label
- Press ESC to exit

Usage:
    python video_capture_test.py
    
Requirements:
    - opencv-python
    - mediapipe
    - tensorflow
    - numpy
"""

import cv2
import mediapipe as mp
from predictor import predict_emotion
from model import load_model, emotion_label_to_string

# Initialize MediaPipe Face Detection
mp_face_detection = mp.solutions.face_detection
mp_drawing = mp.solutions.drawing_utils

# Load TensorFlow Lite model
interpreter = load_model()
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()

# Initialize face detection with confidence threshold
face_detection = mp_face_detection.FaceDetection(model_selection=0, min_detection_confidence=0.5)

# Open webcam
cap = cv2.VideoCapture(0)

frame_count = 0
last_emotion = None 

# Main video processing loop
while True:
    ret, frame = cap.read()
    if not ret:
        break

    frame_count += 1

    # Get frame dimensions and convert to RGB for MediaPipe
    h, w, _ = frame.shape
    frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    results = face_detection.process(frame_rgb)

    # Process detected faces
    if results.detections:
        for detection in results.detections:
            # Convert relative bounding box to absolute coordinates
            bboxC = detection.location_data.relative_bounding_box
            x, y, w_box, h_box = int(bboxC.xmin * w), int(bboxC.ymin * h), \
                                 int(bboxC.width * w), int(bboxC.height * h)

            # Apply boundary checks
            x, y = max(0, x), max(0, y)
            w_box, h_box = min(w - x, w_box), min(h - y, h_box)
            
            # Predict emotion every 10 frames (performance optimization)
            if frame_count % 10 == 0:
                last_emotion = predict_emotion(
                    interpreter,
                    input_details,
                    output_details,
                    frame,
                    (x, y, w_box, h_box)
                )

            # Convert emotion label to string
            emotion_string = emotion_label_to_string(last_emotion) if last_emotion is not None else "Unknown"

            # Draw bounding box and emotion label
            cv2.rectangle(frame, (x, y), (x+w_box, y+h_box), (0, 255, 0), 2)
            cv2.putText(frame, f"Emotion: {emotion_string}({last_emotion})", (x, y - 10),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 255, 255), 2)

    # Display the frame
    cv2.imshow("Real-Time Emotion Detection", frame)

    # Exit on ESC key
    if cv2.waitKey(1) & 0xFF == 27:
        break

# Cleanup
cap.release()
cv2.destroyAllWindows()
