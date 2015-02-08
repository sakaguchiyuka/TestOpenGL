precision highp float;
uniform vec2 u_resolution;

// Repetition function(反復関数)
vec3 opRep(vec3 p) {
  vec3 c = vec3(8.0);
  return mod(p,c) - 0.5*c;
}

// distance functionの定義
const float radius = 1.0;
float distanceFunc(vec3 pos) {
  return length(opRep(pos)) - radius;
}

// 座標の法線ベクトル取得関数
vec3 getNormal(vec3 p){
  const float d = 0.0001;  // 微小変化量
  return normalize(vec3(
    distanceFunc(p + vec3(  d, 0.0, 0.0)) - distanceFunc(p + vec3( -d, 0.0, 0.0)),
    distanceFunc(p + vec3(0.0,   d, 0.0)) - distanceFunc(p + vec3(0.0,  -d, 0.0)),
    distanceFunc(p + vec3(0.0, 0.0,   d)) - distanceFunc(p + vec3(0.0, 0.0,  -d))
  ));
}

const vec3 lightDir = vec3(-0.577, 0.577, 0.577);  // 平行光源の光線方向ベクトル
const int loopCnt = 32;
void main() {
  // フラグメントの座標の算出
  vec2 pos = (gl_FragCoord.xy*2.0 - u_resolution) / min(u_resolution.x, u_resolution.y);

  // カメラとレイの定義
  vec3 camPos = vec3(0.0, 0.0,  2.0);
  vec3 camDir = vec3(0.0, 0.0, -1.0);
  vec3 camUp  = vec3(0.0, 1.0,  0.0);
  vec3 camSide = cross(camDir, camUp);
  float targetDepth = 1.0;
  vec3 rayDir = normalize(camSide*pos.x + camUp*pos.y + camDir*targetDepth);

  // マーチングループ
  float dist   = 0.0;
  float len    = 0.0;
  vec3  rayPos = camPos;
  for (int i = 0; i < loopCnt; i++) {
    dist = distanceFunc(rayPos);
    len += dist;
    rayPos = camPos + rayDir*len;
  }

  if (abs(dist) < 0.001) {
    // ヒットした座標の法線ベクトル計算
    vec3 normal = getNormal(rayPos);
    // とりあえずランバート反射で色を付けてみる
    float diffuse = clamp(dot(lightDir, normal), 0.0, 1.0);
    gl_FragColor = vec4(vec3(diffuse), 1.0);
  } else {
    gl_FragColor = vec4(vec3(0.0), 1.0);
  }
}
