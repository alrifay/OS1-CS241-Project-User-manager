package user_manager;

import java.util.HashMap;

public class User {

//<editor-fold defaultstate="collapsed" desc="Attributes">
    private int _ID;
    private int _minExpireDays;
    private int _maxExpireDays;
    private int _warningExpireDays;
    private String _roomNumber;
    private String _name;
    private String _username;
    private String _homeDir;
    private String _phoneNumber;
    private String _homePhoneNumber;
    private String _otherInformation;
    private String _Password;
    private HashMap<Integer, String> _groups;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Constuctor">
    public User() {
        this._name = "";
        this._ID = 0;
        this._minExpireDays = 0;
        this._maxExpireDays = 0;
        this._warningExpireDays = 0;
        this._roomNumber = "";
        this._username = "";
        this._homeDir = "";
        this._phoneNumber = "";
        this._homePhoneNumber = "";
        this._otherInformation = "";
        this._Password = "";
        this._groups = new HashMap<>();
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Getters">
    public int getID() {
        return _ID;
    }

    public int getMinExpireDays() {
        return _minExpireDays;
    }

    public int getMaxExpireDays() {
        return _maxExpireDays;
    }

    public int getWarningExpireDays() {
        return _warningExpireDays;
    }

    public String getRoomNumber() {
        return _roomNumber;
    }

    public String getName() {
        return _name;
    }

    public String getUsername() {
        return _username;
    }

    public String getHomeDir() {
        return _homeDir;
    }

    public String getPhoneNumber() {
        return _phoneNumber;
    }

    public String getHomePhoneNumber() {
        return _homePhoneNumber;
    }

    public String getOtherInformation() {
        return _otherInformation;
    }

    public String getPassword() {
        return _Password;
    }

    public HashMap<Integer, String> getGroups() {
        return _groups;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Setters">
    public User setID(int _ID) {
        this._ID = _ID;
        return this;
    }

    public User setMinExpireDays(int _minExpireDays) {
        this._minExpireDays = _minExpireDays;
        return this;
    }

    public User setMaxExpireDays(int _maxExpireDays) {
        this._maxExpireDays = _maxExpireDays;
        return this;
    }

    public User setWarningExpireDays(int _warningExpireDays) {
        this._warningExpireDays = _warningExpireDays;
        return this;
    }

    public User setRoomNumber(String _roomNumber) {
        this._roomNumber = _roomNumber;
        return this;
    }

    public User setName(String _name) {
        this._name = _name;
        return this;
    }

    public User setUsername(String _username) {
        this._username = _username;
        return this;
    }

    public User setHomeDir(String _home_dir) {
        this._homeDir = _home_dir;
        return this;
    }

    public User setPhoneNumber(String _phoneNumber) {
        this._phoneNumber = _phoneNumber;
        return this;
    }

    public User setHomePhoneNumber(String _homePhoneNumber) {
        this._homePhoneNumber = _homePhoneNumber;
        return this;
    }

    public User setOtherInformation(String _otherInformation) {
        this._otherInformation = _otherInformation;
        return this;
    }

    public User setPassword(String _Password) {
        this._Password = _Password;
        return this;
    }

    public User setGroups(HashMap<Integer, String> _groups) {
        this._groups = _groups;
        return this;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Override Methods">
    @Override
    public String toString() {
        return this._username;
    }

//</editor-fold>

    @Override
    public User clone() throws CloneNotSupportedException {
        return new User().setGroups( _groups).setHomeDir(_homeDir).setHomePhoneNumber(_homePhoneNumber)
                .setID(_ID).setMaxExpireDays(_maxExpireDays).setMinExpireDays(_minExpireDays).setName(_name)
                .setOtherInformation(_otherInformation).setPassword(_Password).setPhoneNumber(_phoneNumber)
                .setRoomNumber(_roomNumber).setUsername(_username).setWarningExpireDays(_warningExpireDays);
    }

}
