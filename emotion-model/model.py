"""Emotion Model Utilities

This module provides utilities for loading and working with the TensorFlow Lite
emotion recognition model.

The model predicts 7 emotions from 48x48 grayscale face images:
- Angry (0)
- Disgust (1)
- Fear (2)
- Happy (3)
- Neutral (4)
- Sad (5)
- Surprise (6)
"""

import tensorflow as tf


def load_model():
    """Load TensorFlow Lite emotion recognition model.
    
    Loads the emotion_model.tflite file and allocates tensors for inference.
    
    Returns:
        tf.lite.Interpreter: TensorFlow Lite interpreter with loaded model
        
    Raises:
        IOError: If emotion_model.tflite file not found
    """
    interpreter = tf.lite.Interpreter(model_path="emotion_model.tflite")
    interpreter.allocate_tensors()
    return interpreter


def emotion_label_to_string(label):
    """Convert emotion label index to string name.
    
    Maps numerical emotion predictions to human-readable labels.
    
    Args:
        label (int): Emotion label index (0-6)
        
    Returns:
        str: Emotion name string, or "Unknown" if label invalid
        
    Example:
        >>> emotion_label_to_string(3)
        'Happy'
    """
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