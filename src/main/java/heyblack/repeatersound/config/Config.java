package heyblack.repeatersound.config;

public class Config
{
    public float basePitch = 0.5f;
    public boolean useRandomPitch = false;
    public float volume = 0.3f;

    public void setDefault()
    {
        basePitch = 0.5f;
        useRandomPitch = false;
        volume = 0.3f;
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

    public float getVolume()
    {
        return volume;
    }

    public void setVolume(float v)
    {
        volume = v;
    }
}