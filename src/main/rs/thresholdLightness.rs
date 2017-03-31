#pragma version(1)
#pragma rs java_package_name(newera.EliJ)

int size = 7
const int treshLight[size] = {0, 40, 80, 120, 160, 200, 255};

static int nearestLight(float l){

    int index = 0;
    for(int i = 1; i < size; i++){
        if( fabs(threshLight[i] - l) < fabs(threshLight[index] - l) )
            index = i;
    }
    return threshLight[index];
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