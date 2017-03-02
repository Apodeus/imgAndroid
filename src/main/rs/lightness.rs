#pragma version(1)
#pragma rs java_package_name(newera.EliJ)

#include "utility.rsh"

float brightness;

static float restreinColor(float color){
    if(color > 255){
        return 255.0f;
    }
    if(color < 0){
        return 0;
    }
    return color;

}

static float3 changeLightness(float3 pixel, float brightness){

    pixel.r *= brightness;
    pixel.g *= brightness;
    pixel.b *= brightness;

    pixel.r = restreinColor(pixel.r);
    pixel.g = restreinColor(pixel.g);
    pixel.b = restreinColor(pixel.b);

    float3 rgb = {pixel.r, pixel.g, pixel.b};
    return rgb;
}



//MAINS
uchar4 __attribute__((kernel)) ChangeLightness(uchar4 in, uint32_t x, uint32_t y) {
  float3 rgb = {in.r, in.g, in.b};
  float3 new = changeLightness(rgb, brightness);

  uchar4 out;
  out.a = in.a;

  out.r = (uchar)(new.x);
  out.g = (uchar)(new.y);
  out.b = (uchar)(new.z);

  return out;
}