#pragma version(1)
#pragma rs java_package_name(newera.myapplication)

const static float3 gBlackWhiteMult = {0.299f, 0.587f, 0.114f};

//MAINS
uchar4 __attribute__((kernel)) black_and_white(uchar4 in, uint32_t x, uint32_t y) {
  uchar grayscale = in.r * gBlackWhiteMult.r +  in.g * gBlackWhiteMult.g + in.b * gBlackWhiteMult.b;
  uchar4 out;
  out.a = in.a;
  out.r = grayscale;
  out.g = grayscale;
  out.b = grayscale;
  return out;
}