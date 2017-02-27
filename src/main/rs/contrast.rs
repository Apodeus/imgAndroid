#pragma version(1)
#pragma rs java_package_name(newera.myapplication)

float contrast;

static float restrein(float v){
    if(v < 0)
        return 0;
    if(v > 255.0f)
        return 255.0f;
    return v;
}

//MAINS
uchar4 __attribute__((kernel)) Contrast(uchar4 in, uint32_t x, uint32_t y) {
  float3 pixel = {in.r, in.g, in.b};

  float factor = (259.0f * (contrast + 255.0f)) / (255.0f * (259 - contrast));

  uchar4 out;
  out.a = in.a;

  pixel.x = restrein(factor * (pixel.x - 128) + 128);
  pixel.y = restrein(factor * (pixel.y - 128) + 128);
  pixel.z = restrein(factor * (pixel.z - 128) + 128);

  out.r = (uchar)pixel.x;
  out.g = (uchar)pixel.y;
  out.b = (uchar)pixel.z;

  return out;
}

/*
factor = (259 * (contrast + 255)) / (255 * (259 - contrast))

colour = GetPixelColour(x, y)

newRed   = Truncate(factor * (Red(colour)   - 128) + 128)
newGreen = Truncate(factor * (Green(colour) - 128) + 128)
newBlue  = Truncate(factor * (Blue(colour)  - 128) + 128)
PutPixelColour(x, y) = RGB(newRed, newGreen, newBlue)
*/