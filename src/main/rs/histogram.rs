#pragma version(1)
#pragma rs_fp_relaxed
#pragma rs java_package_name(newera.myapplication)

const static float3 grayScale = {0.299f, 0.587f, 0.114f};

int32_t histogram[256];
float remapArray[256];
int size;

//restrein the value val between 0 and 1
static float restrein(float val){
    float f = fmax(0.0f, val);
    f = min(1.0f, f);
    return f;
}

void initArray() {
    //init the array with zeros
    for (int i = 0; i < 256; i++) {
        histogram[i] = 0;
        remapArray[i] = 0.0f;
    }
}

void createRemapArray() {
    //create map for y
    float sum = 0;
    for (int i = 0; i < 256; i++) {
        sum += histogram[i];
        remapArray[i] = sum / (size);
    }
}


//MAINS
uchar4 __attribute__((kernel)) calculHistogram(uchar4 in, uint32_t x, uint32_t y) {
  uchar4 out;
  float4 pixel = rsUnpackColor8888(in);

  float Y = grayScale.r * pixel.r + grayScale.g * pixel.g + grayScale.b * pixel.b;
  float U = ((0.492f * (pixel.b - Y)) + 1.0f) / 2.0f;
  float V = ((0.877f * (pixel.r - Y)) + 1.0f) / 2.0f;

  int32_t Yvalue = Y * 255;

  histogram[Yvalue]++;

  float4 new = {Y, U, V, pixel.a};
  out = rsPackColorTo8888(new);
  return out;
}

uchar4 __attribute__((kernel)) YUVToRGB(uchar4 in, uint32_t x, uint32_t y) {

    float4 pixel = rsUnpackColor8888(in);

    float Y = pixel.r;

    int32_t Yvalue = Y * 255;

    Y = remapArray[Yvalue];

    float U = (2.0f * pixel.g) - 1.0f;
    float V = (2.0f * pixel.b) - 1.0f;

    //Compute values for red, green and blue channels
    float red = restrein(Y + 1.14f * V);
    float green = restrein(Y - 0.395f * U - 0.581f * V);
    float blue = restrein(Y + 2.033f * U);

    //Put the values in the output uchar4
    return rsPackColorTo8888(red, green, blue, pixel.a);
}
