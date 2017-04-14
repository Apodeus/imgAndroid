#pragma version(1)
#pragma rs java_package_name(newera.EliJ)

#include "utility.rsh"

int size = 5;
const int treshLight[5] = {0, 60, 120, 180,  255};

static int nearestLight(float l){

    int index = 0;
    for(int i = 1; i < size; i++){
        if( fabs(treshLight[i] - l) < fabs(treshLight[index] - l) )
           index = i;
    }
    return treshLight[index];
    return 1;
}

static float3 tresh(float r, float g, float b){
    float3 color = {r, g, b};
    color = RgbToHsl(color);

    color.z = (float)nearestLight(color.z);//Lightness;
    if(color.z < 0)
        color.z = 0;
    if(color.z >= 255.0f)
        color.z = 255.0f;
    color = HslToRgb(color);
    return color;

}

//MAINS
uchar4 __attribute__((kernel)) treshold(uchar4 in, uint32_t x, uint32_t y) {
  uchar4 out;
  float3 newColor = tresh(in.r, in.g, in.b);
  out.a = in.a;
  out.r = newColor.x;
  out.g = newColor.y;
  out.b = newColor.z;
  return out;
}