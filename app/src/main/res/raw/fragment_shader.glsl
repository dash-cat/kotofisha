#version 100
precision mediump float;
varying vec4 fColor;
uniform float uTime; // Временная переменная для анимации

void main() {
    float distanceFromCenter = length(gl_PointCoord - vec2(0.5, 0.5));
    float alpha = 1.0 - smoothstep(0.0, 0.5, distanceFromCenter);

    // Динамическое изменение радиуса горизонта событий и свечения
    float eventHorizonRadius = 0.2 + sin(uTime * 0.5) * 0.05;
    float glowRadius = 0.3 + cos(uTime * 0.5) * 0.05;
    float distanceFromEventHorizon = abs(distanceFromCenter - eventHorizonRadius);

    vec3 eventHorizonColor = vec3(0.0); // Цвет горизонта событий
    vec3 glowColor = vec3(1.0, 0.8, 0.6) * (1.0 - distanceFromEventHorizon / glowRadius);

    vec3 finalColor;
    if (distanceFromCenter < eventHorizonRadius) {
        finalColor = eventHorizonColor;
    } else if (distanceFromCenter < glowRadius) {
        finalColor = mix(glowColor, fColor.rgb, distanceFromEventHorizon / glowRadius);
    } else {
        finalColor = fColor.rgb;
    }

    gl_FragColor = vec4(finalColor, alpha);
}