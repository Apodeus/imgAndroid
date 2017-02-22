#pragma version(1)
#pragma rs java_package_name(newera.myapplication)

const static float3 gBlackWhiteMult = {0.299f, 0.587f, 0.114f};

static float hue2rgb(float p, float q, float t){
    if(t < 0)
        t += 1.0f;
    if(t > 1.0f)
        t -= 1.0f;
    if(t < 1.0f/6.0f)
        return p + (q - p) * 6.0f * t;
    if(t < 1.0f/2.0f)
        return q;
    if(t < 2.0f/3.0f)
        return p + (q - p) * (2.0f/3.0f - t) * 6.0f;
    return p;
}

static float restreinHue(float h){
    float newHue = h;
    if (newHue < 0){
        newHue = 1.0f - fabs(fmod(newHue, 1.0f));
    }
    if(newHue > 1.0f){
        newHue = 0 + fabs(fmod(newHue, 1.0f));
    }
}

static int3 convertGrayScale(int r, int g, int b){
      int grayscale = r * gBlackWhiteMult.r +  g * gBlackWhiteMult.g + b * gBlackWhiteMult.b;
      int3 new;
      new.x = grayscale;
      new.y = grayscale;
      new.z = grayscale;
      return new;
}

static float3 hslToRGB(float h, float s, float l){
    //float4 pixel;
    float r, g, b;
    if(s == 0){
        r = l;
        g = l;
        b = l;
    }else{
        float p, q;
        if(l < 0.5f){
            q =  l * (1.0f + s) ;
        } else {
            q = l + s - l * s;
        }
        p = 2.0f * l - q;

        r = hue2rgb(p, q, h + 1.0f/3.0f);
        g = hue2rgb(p, q, h);
        b = hue2rgb(p, q, h - 1.0f/3.0f);
    }
    float3 rgb = {r, g, b};
    return rgb;
}


static float3 rgbToHsl(float r, float g, float b){
    float r_, g_, b_;
    float valMax, valMin;
    float h, s, l;

    r_ = (float)(r) / 255.0f;
    g_ = (float)(g) / 255.0f;
    b_ = (float)(b) / 255.0f;

    valMax = max(max(r_, g_), b_);
    valMin = min(min(r_, g_), b_);

    h = (valMax + valMin) / 2.0f;
    s = (valMax + valMin) / 2.0f;
    l = (valMax + valMin) / 2.0f;

    if(valMax == valMin){
        h = 0;
        s = 0;
    }else{
        float delta = valMax - valMin;
        if(l > 0.5f){
            s = delta / (2.0f - valMax - valMin);
        } else {
            s = delta / (valMax + valMin);
        }

        if(valMax == r_){
            if(g_ < b_){
                h = (g_ - b_) / delta + 6.0f;
            } else {
                h = (g_ - b_) / delta;
            }
        } else if(valMax == g_){
            h = (b_ - r_) / delta + 2.0f;
        } else {
            h = (r_ - g_) / delta + 4.0f;
        }

        h = h / 6.0f;
    }

    float3 hsl = {h, s, l};

    return hsl;
}

