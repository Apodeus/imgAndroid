#pragma version(1)
#pragma rs java_package_name(newera.myapplication)

#include "utility.rsh"

float factor;

static float3 changeLightness(float3 pixel, float factor){

    float3 hsl = RgbToHsl(pixel);

    hsl.z = hsl.z * factor;
    /*
    if(hsl.z >= 1.0f){
       hsl.z = 1.0f;
    }
    if(hsl.z < 0){
       hsl.z = 0;
    }*/

    float3 rgb = HslToRgb(hsl);

    return rgb;

}



//MAINS
uchar4 __attribute__((kernel)) ChangeLightness(uchar4 in, uint32_t x, uint32_t y) {
  float3 rgb = {in.r, in.g, in.b};
  float3 new = changeLightness(rgb, factor);

  uchar4 out;
  out.a = in.a;

  out.r = (uchar)(new.x);
  out.g = (uchar)(new.y);
  out.b = (uchar)(new.z);

  return out;
}