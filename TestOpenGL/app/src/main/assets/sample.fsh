precision highp float;
uniform vec2 u_resolution;

void main() {
    vec2 pos = (gl_FragCoord.xy*2.0 - u_resolution) / min(u_resolution.x, u_resolution.y);
    gl_FragColor = vec4(pos, 0.0, 1.0);
}