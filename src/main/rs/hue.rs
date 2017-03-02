#pragma version(1)
#pragma rs java_package_name(newera.EliJ)

#include "utility.rsh"

float newHue;
static float3 changeHue(float3 pixel, float factor){
    float3 hsl = RgbToHsl(pixel);
    //hue is between 0:360
    hsl.x = restreinHue(factor);

    hsl.x = restreinHue(hsl.x);

    float3 new = HslToRgb(hsl);

    return new;
}



//MAINS
uchar4 __attribute__((kernel)) ChangeHue(uchar4 in, uint32_t x, uint32_t y) {
  float3 rgb = {in.r, in.g, in.b};
  float3 new = changeHue(rgb, newHue);

  uchar4 out;
  out.a = in.a;
  out.r = (uchar)(new.x);
  out.g = (uchar)(new.y);
  out.b = (uchar)(new.z);

  return out;
}
