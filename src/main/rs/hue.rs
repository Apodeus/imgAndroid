#pragma version(1)
#pragma rs java_package_name(newera.myapplication)

#include "utility.rsh"

float factor;
static float3 changeHue(float3 pixel, float factor){
    float3 hsl = rgbToHsl(pixel.r, pixel.g, pixel.b);
    //hue is between [0;2.0[
    hsl.x = factor;

    hsl.x = restreinHue(hsl.x);

    float3 new = hslToRGB(hsl.x, hsl.y, hsl.z);

    //new.a = pixel.a;
    return new;
}



//MAINS
uchar4 __attribute__((kernel)) ChangeHue(uchar4 in, uint32_t x, uint32_t y) {
  //float4 pixel = rsUnpackColor8888(in);
  float3 rgb = {in.r, in.g, in.b};
  float3 new = changeHue(rgb, factor);

  uchar4 out;
  out.a = in.a;
  out.r = (uchar)(new.r * 255.0f);
  out.g = (uchar)(new.g * 255.0f);
  out.b = (uchar)(new.b * 255.0f);

  return out;
}