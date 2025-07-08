package com.keensense.admin.picturestream.entity;

import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.StringUtil;

public class PictureResult implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;

    private String license = "";
    private int plateColorCode = 0;
    private String plateColor = "";
    private int plateType = 0;
    private int confidence = 0;

    private int bright = 0;
    private int direction = 0;
    private int locationLeft = 0;
    private int locationTop = 0;
    private int locationRight = 0;
    private int locationBottom = 0;

    private int costTime = 0;
    private String carBright = "";
    private int carColorCode = 0;
    private String carColor = "";
    private String carLogo = "";
    private long frameIndex = 0l;
    private double carspeed = 0.0;
    private String labelInfoData = "";
    private int vehicleKindCode = 0;
    private String vehicleKind = "";
    private String vehicleBrand = "";
    private String vehicleSeries = "";
    private String vehicleStyle = "";

    private int tag;
    private int paper;
    private int mainSun;
    private int secondSun;
    private int sun;
    private int drop;
    private int mainBelt;
    private int secondPeople;
    private int secondBelt;
    private int call;
    private int crash;
    private int danger;
    
    private int convertible; // 三轮车是否敞篷
    private int manned; // 三轮车是否载人

    private int headstock;
    private int topWindow;
    private int carrier;
    private int bodyChar;
    private int spareTire; // 备胎
    
    private int vehicleLeft;
    private int vehicleTop;
    private int vehicleRight;
    private int vehicleBottom;
    private int vehicleConfidence;

    private int windowLeft;
    private int windowTop;
    private int windowRight;
    private int windowBottom;

    private String tagRect = "";
    private String paperRect = "";
    private String sunRect = "";
    private String dropRect = "";
    private String windowRect = "";

    private String tagScore = "";
    private String paperScore = "";
    private String sunScore = "";
    private String dropScore = "";
    private String windowScore = "";

    private String featureUrl = "";

    private String similar = "";

    public static PictureResult getResultByJson(Var var)
    {
        PictureResult result = new PictureResult();
        result.license = var.getString("Recognize.Plate.Licence");
        result.plateColorCode = var.getInt("Recognize.Plate.Color.Code");
        result.plateColor = var.getString("Recognize.Plate.Color.Name");
        result.plateType = var.getInt("Recognize.Plate.Type");
        result.confidence = var.getInt("Recognize.Plate.Score");

        // 车牌位置
        result.locationLeft = var.getInt("Detect.Plate.Rect[0]");
        result.locationTop = var.getInt("Detect.Plate.Rect[1]");
        result.locationRight = result.locationLeft
                + var.getInt("Detect.Plate.Rect[2]");
        result.locationBottom = result.locationTop
                + var.getInt("Detect.Plate.Rect[3]");

        result.carColorCode = StringUtil
                .getInteger(var.getString("Recognize.Color.TopList[0].Code"));
        result.carColor = var.getString("Recognize.Color.TopList[0].Name");
        result.vehicleKindCode = StringUtil
                .getInteger(var.getString("Recognize.Type.TopList[0].Code"));
        result.vehicleKind = var.getString("Recognize.Type.TopList[0].Name");
        String tmpBrand = var.getString("Recognize.Brand.TopList[0].Name");
        if (StringUtil.isNotNull(tmpBrand))
        {
            String[] tmpBrandArr = tmpBrand.split("-");
            if (tmpBrandArr.length > 0)
            {
                result.vehicleBrand = tmpBrandArr[0];
                result.carLogo = tmpBrandArr[0];
            }
            if (tmpBrandArr.length > 1)
            {
                result.vehicleSeries = tmpBrandArr[1];
            }
            if (tmpBrandArr.length > 2)
            {
                result.vehicleStyle = tmpBrandArr[2];
            }
        }

        result.tag = var.getInt("Recognize.Marker.Tag.size") > 0 ? 1 : 0;
        result.paper = var.getInt("Recognize.Marker.Paper.size") > 0 ? 1 : 0;
        result.sun = var.getInt("Recognize.Marker.Sun.size") > 0 ? 1 : 0;
        result.drop = var.getInt("Recognize.Marker.Drop.size") > 0 ? 1 : 0;
        result.mainBelt = var.getBoolean("Recognize.Belt.MainDriver.NoBelt") ? 0
                : 1;
        result.secondBelt = var.getBoolean("Recognize.Belt.CoDriver.NoBelt") ? 0
                : 1;
        result.call = var.getBoolean("Recognize.Call.HasCall") ? 1 : 0;
        result.crash = var.getBoolean("Recognize.Crash.HasCrash") ? 1 : 0;
        result.danger = var.getBoolean("Recognize.Danger.HasDanger") ? 1 : 0;
        
        result.headstock = var.getInt("Recognize.Mistake");
        result.topWindow = var.getString("Recognize.Sunroof.TopList[0].name").equalsIgnoreCase("have")?1:0;
        result.carrier = var.getString("Recognize.Rack.TopList[0].name").equalsIgnoreCase("have")?1:0;
        result.bodyChar = 0;
        result.convertible = 0;
        result.manned = 0;
        result.spareTire = var.getBoolean("Recognize.SpareTire.SpareTire")? 1 : 0;

        // 车身位置
        result.vehicleLeft = var.getInt("Detect.Body.Rect[0]");
        result.vehicleTop = var.getInt("Detect.Body.Rect[1]");
        result.vehicleRight = result.vehicleLeft
                + var.getInt("Detect.Body.Rect[2]");
        result.vehicleBottom = result.vehicleTop
                + var.getInt("Detect.Body.Rect[3]");
        result.vehicleConfidence = var
                .getInt("Recognize.Brand.TopList[0].Score");

        // 车窗位置
        result.windowRect = var.getString("Detect.Window.Rect");
        result.windowLeft = var.getInt("Detect.Window.Rect[0]");
        result.windowTop = var.getInt("Detect.Window.Rect[1]");
        result.windowRight = result.windowLeft + var.getInt("Detect.Window.Rect[2]");
        result.windowBottom = result.windowTop + var.getInt("Detect.Window.Rect[3]");
        result.windowScore = "[" + var.getString("Detect.Window.Score") + "]";
        
        int windowWidth = var.getInt("Detect.Window.Rect[2]");

        result.tagRect = "";
        result.tagScore = "";
        int tagCount = var.getInt("Recognize.Marker.Tag.size");
        for (int i = 0; i < tagCount && i < 8; i++)
        {
            if (0 == i)
            {
                result.tagRect += "[";
                result.tagScore += "[";
            }
            else
            {
                result.tagRect += ",";
                result.tagScore += ",";
            }
            result.tagRect += result.getMarkerRect(
                    var.getString("Recognize.Marker.Tag[" + i + "].Rect"));
            result.tagScore += var
                    .getString("Recognize.Marker.Tag[" + i + "].Score");
            if (tagCount - 1 == i)
            {
                result.tagRect += "]";
                result.tagScore += "]";
            }
        }
        result.paperRect = "";
        result.paperScore = "";
        int paperCount = var.getInt("Recognize.Marker.Paper.size");
        for (int i = 0; i < paperCount && i < 2; i++)
        {
            if (0 == i)
            {
                result.paperRect += "[";
                result.paperScore += "[";
            }
            else
            {
                result.paperRect += ",";
                result.paperScore += ",";
            }
            result.paperRect += result.getMarkerRect(
                    var.getString("Recognize.Marker.Paper[" + i + "].Rect"));
            result.paperScore += var
                    .getString("Recognize.Marker.Paper[" + i + "].Score");
            if (paperCount - 1 == i)
            {
                result.paperRect += "]";
                result.paperScore += "]";
            }
        }
        result.sunRect = "";
        result.sunScore = "";
        int sunCount = var.getInt("Recognize.Marker.Sun.size");
        for (int i = 0; i < sunCount && i < 2; i++)
        {
            if (0 == i)
            {
                result.sunRect += "[";
                result.sunScore += "[";
            }
            else
            {
                result.sunRect += ",";
                result.sunScore += ",";
            }
            int sunLeft = var.getInt("Recognize.Marker.Sun[" + i + "].Rect[0]");
            if (sunLeft < windowWidth / 3)
            {
                result.mainSun = 1;
            }
            else
            {
                result.secondSun = 1;
            }
            result.sunRect += result.getMarkerRect(
                    var.getString("Recognize.Marker.Sun[" + i + "].Rect"));
            result.sunScore += var
                    .getString("Recognize.Marker.Sun[" + i + "].Score");
            if (sunCount - 1 == i)
            {
                result.sunRect += "]";
                result.sunScore += "]";
            }
        }
        result.dropRect = "";
        result.dropScore = "";
        int dropCount = var.getInt("Recognize.Marker.Drop.size");
        for (int i = 0; i < dropCount && i < 2; i++)
        {
            if (0 == i)
            {
                result.dropRect += "[";
                result.dropScore += "[";
            }
            else
            {
                result.dropRect += ",";
                result.dropScore += ",";
            }
            result.dropRect += result.getMarkerRect(
                    var.getString("Recognize.Marker.Drop[" + i + "].Rect"));
            result.dropScore += var
                    .getString("Recognize.Marker.Drop[" + i + "].Score");
            if (dropCount - 1 == i)
            {
                result.dropRect += "]";
                result.dropScore += "]";
            }
        }

        result.similar = var.getString("Recognize.Similar.Feature");

        return result;
    }

    public String getLicense()
    {
        return license;
    }

    public void setLicense(String license)
    {
        this.license = license;
    }

    public String getPlateColor()
    {
        return plateColor;
    }

    public void setPlateColor(String plateColor)
    {
        this.plateColor = plateColor;
    }

    public int getPlateType()
    {
        return plateType;
    }

    public void setPlateType(int plateType)
    {
        this.plateType = plateType;
    }

    public int getConfidence()
    {
        return confidence;
    }

    public void setConfidence(int confidence)
    {
        this.confidence = confidence;
    }

    public int getBright()
    {
        return bright;
    }

    public void setBright(int bright)
    {
        this.bright = bright;
    }

    public int getDirection()
    {
        return direction;
    }

    public void setDirection(int direction)
    {
        this.direction = direction;
    }

    public int getLocationLeft()
    {
        return locationLeft;
    }

    public void setLocationLeft(int locationLeft)
    {
        this.locationLeft = locationLeft;
    }

    public int getLocationTop()
    {
        return locationTop;
    }

    public void setLocationTop(int locationTop)
    {
        this.locationTop = locationTop;
    }

    public int getLocationRight()
    {
        return locationRight;
    }

    public void setLocationRight(int locationRight)
    {
        this.locationRight = locationRight;
    }

    public int getLocationBottom()
    {
        return locationBottom;
    }

    public void setLocationBottom(int locationBottom)
    {
        this.locationBottom = locationBottom;
    }

    public int getCostTime()
    {
        return costTime;
    }

    public void setCostTime(int costTime)
    {
        this.costTime = costTime;
    }

    public String getCarBright()
    {
        return carBright;
    }

    public void setCarBright(String carBright)
    {
        this.carBright = carBright;
    }

    public String getCarColor()
    {
        return carColor;
    }

    public void setCarColor(String carColor)
    {
        this.carColor = carColor;
    }

    public String getCarLogo()
    {
        return carLogo;
    }

    public void setCarLogo(String carLogo)
    {
        this.carLogo = carLogo;
    }

    public long getFrameIndex()
    {
        return frameIndex;
    }

    public void setFrameIndex(long frameIndex)
    {
        this.frameIndex = frameIndex;
    }

    public double getCarspeed()
    {
        return carspeed;
    }

    public void setCarspeed(double carspeed)
    {
        this.carspeed = carspeed;
    }

    public String getLabelInfoData()
    {
        return labelInfoData;
    }

    public void setLabelInfoData(String labelInfoData)
    {
        this.labelInfoData = labelInfoData;
    }

    public String getVehicleKind()
    {
        return vehicleKind;
    }

    public void setVehicleKind(String vehicleKind)
    {
        this.vehicleKind = vehicleKind;
    }

    public String getVehicleBrand()
    {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand)
    {
        this.vehicleBrand = vehicleBrand;
    }

    public String getVehicleSeries()
    {
        return vehicleSeries;
    }

    public void setVehicleSeries(String vehicleSeries)
    {
        this.vehicleSeries = vehicleSeries;
    }

    public String getVehicleStyle()
    {
        return vehicleStyle;
    }

    public void setVehicleStyle(String vehicleStyle)
    {
        this.vehicleStyle = vehicleStyle;
    }

    public int getTag()
    {
        return tag;
    }

    public void setTag(int tag)
    {
        this.tag = tag;
    }

    public int getPaper()
    {
        return paper;
    }

    public void setPaper(int paper)
    {
        this.paper = paper;
    }

    public int getSun()
    {
        return sun;
    }

    public void setSun(int sun)
    {
        this.sun = sun;
    }

    public int getDrop()
    {
        return drop;
    }

    public void setDrop(int drop)
    {
        this.drop = drop;
    }

    public int getMainBelt()
    {
        return mainBelt;
    }

    public void setMainBelt(int mainBelt)
    {
        this.mainBelt = mainBelt;
    }

    public int getSecondBelt()
    {
        return secondBelt;
    }

    public void setSecondBelt(int secondBelt)
    {
        this.secondBelt = secondBelt;
    }

    public int getCall()
    {
        return call;
    }

    public void setCall(int call)
    {
        this.call = call;
    }

    public int getCrash()
    {
        return crash;
    }

    public void setCrash(int crash)
    {
        this.crash = crash;
    }

    public int getDanger()
    {
        return danger;
    }

    public void setDanger(int danger)
    {
        this.danger = danger;
    }

    public int getVehicleLeft()
    {
        return vehicleLeft;
    }

    public void setVehicleLeft(int vehicleLeft)
    {
        this.vehicleLeft = vehicleLeft;
    }

    public int getVehicleTop()
    {
        return vehicleTop;
    }

    public void setVehicleTop(int vehicleTop)
    {
        this.vehicleTop = vehicleTop;
    }

    public int getVehicleRight()
    {
        return vehicleRight;
    }

    public void setVehicleRight(int vehicleRight)
    {
        this.vehicleRight = vehicleRight;
    }

    public int getVehicleBottom()
    {
        return vehicleBottom;
    }

    public void setVehicleBottom(int vehicleBootom)
    {
        this.vehicleBottom = vehicleBootom;
    }

    public int getVehicleConfidence()
    {
        return vehicleConfidence;
    }

    public void setVehicleConfidence(int vehicleConfidence)
    {
        this.vehicleConfidence = vehicleConfidence;
    }

    public int getWindowLeft()
    {
        return windowLeft;
    }

    public void setWindowLeft(int windowLeft)
    {
        this.windowLeft = windowLeft;
    }

    public int getWindowTop()
    {
        return windowTop;
    }

    public void setWindowTop(int windowTop)
    {
        this.windowTop = windowTop;
    }

    public int getWindowRight()
    {
        return windowRight;
    }

    public void setWindowRight(int windowRight)
    {
        this.windowRight = windowRight;
    }

    public int getWindowBottom()
    {
        return windowBottom;
    }

    public void setWindowBottom(int windowBottom)
    {
        this.windowBottom = windowBottom;
    }

    public String getTagRect()
    {
        return tagRect;
    }

    public void setTagRect(String tagRect)
    {
        this.tagRect = tagRect;
    }

    public String getPaperRect()
    {
        return paperRect;
    }

    public void setPaperRect(String paperRect)
    {
        this.paperRect = paperRect;
    }

    public String getSunRect()
    {
        return sunRect;
    }

    public void setSunRect(String sunRect)
    {
        this.sunRect = sunRect;
    }

    public String getDropRect()
    {
        return dropRect;
    }

    public void setDropRect(String dropRect)
    {
        this.dropRect = dropRect;
    }

    public String getWindowRect()
    {
        return windowRect;
    }

    public void setWindowRect(String windowRect)
    {
        this.windowRect = windowRect;
    }

    public String getTagScore()
    {
        return tagScore;
    }

    public void setTagScore(String tagScore)
    {
        this.tagScore = tagScore;
    }

    public String getPaperScore()
    {
        return paperScore;
    }

    public void setPaperScore(String paperScore)
    {
        this.paperScore = paperScore;
    }

    public String getSunScore()
    {
        return sunScore;
    }

    public void setSunScore(String sunScore)
    {
        this.sunScore = sunScore;
    }

    public String getDropScore()
    {
        return dropScore;
    }

    public void setDropScore(String dropScore)
    {
        this.dropScore = dropScore;
    }

    public String getWindowScore()
    {
        return windowScore;
    }

    public void setWindowScore(String windowScore)
    {
        this.windowScore = windowScore;
    }

    public String getFeatureUrl()
    {
        return featureUrl;
    }

    public void setFeatureUrl(String featureUrl)
    {
        this.featureUrl = featureUrl;
    }

    public String getSimilar()
    {
        return similar;
    }

    public void setSimilar(String similar)
    {
        this.similar = similar;
    }

    public int getPlateColorCode()
    {
        return plateColorCode;
    }

    public void setPlateColorCode(int plateColorCode)
    {
        this.plateColorCode = plateColorCode;
    }

    public int getCarColorCode()
    {
        return carColorCode;
    }

    public void setCarColorCode(int carColorCode)
    {
        this.carColorCode = carColorCode;
    }

    public int getVehicleKindCode()
    {
        return vehicleKindCode;
    }

    public void setVehicleKindCode(int vehicleKindCode)
    {
        this.vehicleKindCode = vehicleKindCode;
    }

    public int getMainSun()
    {
        return mainSun;
    }

    public void setMainSun(int mainSun)
    {
        this.mainSun = mainSun;
    }

    public int getSecondSun()
    {
        return secondSun;
    }

    public void setSecondSun(int secondSun)
    {
        this.secondSun = secondSun;
    }

    public int getSecondPeople()
    {
        return secondPeople;
    }

    public void setSecondPeople(int secondPeople)
    {
        this.secondPeople = secondPeople;
    }

    public int getHeadstock()
    {
        return headstock;
    }

    public void setHeadstock(int headstock)
    {
        this.headstock = headstock;
    }

    public int getTopWindow()
    {
        return topWindow;
    }

    public void setTopWindow(int topWindow)
    {
        this.topWindow = topWindow;
    }

    public int getCarrier()
    {
        return carrier;
    }

    public void setCarrier(int carrier)
    {
        this.carrier = carrier;
    }

    public int getBodyChar()
    {
        return bodyChar;
    }

    public void setBodyChar(int bodyChar)
    {
        this.bodyChar = bodyChar;
    }

    public int getConvertible()
    {
        return convertible;
    }

    public void setConvertible(int convertible)
    {
        this.convertible = convertible;
    }

    public int getManned()
    {
        return manned;
    }

    public void setManned(int manned)
    {
        this.manned = manned;
    }

    public int getSpareTire()
    {
        return spareTire;
    }

    public void setSpareTire(int spareTire)
    {
        this.spareTire = spareTire;
    }

    private String getMarkerRect(String rect)
    {
        if (StringUtil.isNull(rect)) return "{}";

        rect = rect.substring(1, rect.length() - 1);
        String[] rectArr = rect.split(",");
        if (rectArr.length != 4) return "{}";
        int x1 = StringUtil.getInteger(rectArr[0]);
        int y1 = StringUtil.getInteger(rectArr[1]);
        int width = StringUtil.getInteger(rectArr[2]);
        int height = StringUtil.getInteger(rectArr[3]);

        Var retVar = Var.newObject();
        retVar.set("x1", this.windowLeft + x1);
        retVar.set("y1", this.windowTop + y1);
        retVar.set("x2", this.windowLeft + x1 + width);
        retVar.set("y2", this.windowTop + y1 + height);
        return retVar.toString();
    }

    public Var toKafkaJson()
    {
        Var retVar = Var.newObject();
        retVar.set("plateNo", this.license);
        retVar.set("plateConfidence", this.confidence);
        retVar.set("plateColor", this.plateColor);
        retVar.set("plateType", this.plateType);
        retVar.set("plateRegion", this.locationLeft + "," + this.locationTop + ","
                + this.locationRight + "," + this.locationBottom);
        retVar.set("carColor", this.carColor);
        retVar.set("vehicleKind", this.vehicleKind);
        retVar.set("vehicleBrand", this.vehicleBrand);
        retVar.set("vehicleSeries", this.vehicleSeries);
        retVar.set("vehicleStyle", this.vehicleStyle);
        retVar.set("vehicleRegion",
                this.getVehicleLeft() + "," + this.getVehicleTop() + ","
                        + this.getVehicleRight() + ","
                        + this.getVehicleBottom());
        retVar.set("vehicleConfidence", this.getVehicleConfidence());
        retVar.set("tag", this.tag);
        retVar.set("paper", this.paper);
        retVar.set("sun", this.sun);
        retVar.set("drop", this.drop);
        retVar.set("mainBelt", this.mainBelt);
        retVar.set("secondBelt", this.secondBelt);
        retVar.set("call", this.call);
        retVar.set("danger", this.danger);
        retVar.set("crash", this.crash);
        retVar.set("featureRegion.tag", this.getTagRect());
        retVar.set("featureRegion.paper", this.getPaperRect());
        retVar.set("featureRegion.sun", this.getSunRect());
        retVar.set("featureRegion.drop", this.getDropRect());
        retVar.set("featureRegion.window", this.getWindowRect());

        return retVar;
    }
}
