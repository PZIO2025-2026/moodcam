import cv2
import mediapipe as mp
from predictor import predict_emotion
from model import load_model, emotion_label_to_string

mp_face_detection = mp.solutions.face_detection
mp_drawing = mp.solutions.drawing_utils

interpreter = load_model()
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()

face_detection = mp_face_detection.FaceDetection(model_selection=0, min_detection_confidence=0.5)

cap = cv2.VideoCapture(0)

frame_count = 0
last_emotion = None 


while True:
    ret, frame = cap.read()
    if not ret:
        break

    frame_count += 1

    h, w, _ = frame.shape
    frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    results = face_detection.process(frame_rgb)

    if results.detections:
        for detection in results.detections:
            bboxC = detection.location_data.relative_bounding_box
            x, y, w_box, h_box = int(bboxC.xmin * w), int(bboxC.ymin * h), \
                                 int(bboxC.width * w), int(bboxC.height * h)

            x, y = max(0, x), max(0, y)
            w_box, h_box = min(w - x, w_box), min(h - y, h_box)
            
            if frame_count % 10 == 0:
                last_emotion = predict_emotion(
                    interpreter,
                    input_details,
                    output_details,
                    frame,
                    (x, y, w_box, h_box)
                )

            emotion_string = emotion_label_to_string(last_emotion) if last_emotion is not None else "Unknown"

            cv2.rectangle(frame, (x, y), (x+w_box, y+h_box), (0, 255, 0), 2)
            cv2.putText(frame, f"Emotion: {emotion_string}({last_emotion})", (x, y - 10),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 255, 255), 2)

    cv2.imshow("Real-Time Emotion Detection", frame)

    if cv2.waitKey(1) & 0xFF == 27:
        break

cap.release()
cv2.destroyAllWindows()
