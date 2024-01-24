#version 100

attribute vec4 vPosition;
attribute vec4 vColor;
attribute float vPointSize;
varying vec4 fColor;
void main() {
    gl_Position = vPosition;
    gl_PointSize = vPointSize;
    fColor = vColor;
}