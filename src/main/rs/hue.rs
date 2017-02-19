#pragma version(1)
#pragma rs java_package_name(newera.myapplication)

#include "utility.rsh"

static float3 changeHue(float3 rgb, float factor){
    float3 hsl = rgbToHsl(rgb.x, rgb.y, rgb.z);

    hsl.x = hsl.x * factor;
    hsl.x = fmod(hsl.x, 6.0f);

    float3 new = hslToRGB(hsl.x, hsl.y, hsl.z);
    return new;
}



//MAINS
uchar4 __attribute__((kernel)) ChangeHue(uchar4 in, uint32_t x, uint32_t y) {
  float3 rgb = {in.r, in.g, in.b};
  float factor = 1.2f;
  float3 new = changeHue(rgb, factor);
  uchar4 out;

  out.a = in.a;
  out.r = (uchar)(new.x * 255.0f);
  out.g = (uchar)(new.y * 255.0f);
  out.b = (uchar)(new.z * 255.0f);

  return out;
}