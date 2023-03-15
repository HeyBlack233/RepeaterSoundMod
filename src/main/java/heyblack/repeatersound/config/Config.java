package heyblack.repeatersound.config;

public class Config
{
    public float basePitch;
    public boolean useRandomPitch;

    public void setDefault()
    {
        basePitch = 0.5f;
        useRandomPitch = false;
    }

    public float getBasePitch()
    {
        return basePitch;
    }

    public void setBasePitch(float f)
    {
        basePitch = f;
    }

    public boolean getRandomPitch()
    {
        return useRandomPitch;
    }

    public void setRandomPitch(boolean bl)
    {
        useRandomPitch = bl;
    }
}