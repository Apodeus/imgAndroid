#pragma version(1)
#pragma rs java_package_name(newera.myapplication)

const static float3 sepiaRed = {0.393f, 0.769f, 0.189f};
const static float3 sepiaGreen = {0.349f, 0.686f, 0.168f};
const static float3 sepiaBlue = {0.272f, 0.534f, 0.131f};

//MAINS
uchar4 __attribute__((kernel)) sepia(uchar4 in, uint32_t x, uint32_t y) {

  uchar red   = min((int)(in.r * sepiaRed.r   + in.g * sepiaRed.g   + in.b * sepiaRed.b)  , 255);
  uchar green = min((int)(in.r * sepiaGreen.r + in.g * sepiaGreen.g + in.b * sepiaGreen.b), 255);
  uchar blue  = min((int)(in.r * sepiaBlue.r  + in.g * sepiaBlue.g  + in.b * sepiaBlue.b) , 255);

  uchar4 out;
  out.a = in.a;
  out.r = red;
  out.g = green;
  out.b = blue;
  return out;
}