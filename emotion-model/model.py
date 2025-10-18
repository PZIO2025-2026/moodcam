import tensorflow as tf

def load_model():
    interpreter = tf.lite.Interpreter(model_path="emotion_model.tflite")
    interpreter.allocate_tensors()
    return interpreter

def emotion_label_to_string(label):
    emotions = {
        0: "Angry",
        1: "Disgust",
        2: "Fear",
        3: "Happy",
        4: "Neutral",
        5: "Sad",
        6: "Surprise"
    }
    return emotions.get(label, "Unknown")