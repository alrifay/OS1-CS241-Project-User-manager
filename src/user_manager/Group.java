package user_manager;

import java.util.ArrayList;

public class Group {

    private String _name;
    private int _ID;
    private ArrayList<String> _users;

    public Group() {
        this._name = "";
        this._ID = 0;
        this._users = new ArrayList<>();
    }

    public String getName() {
        return _name;
    }

    public Group setName(String _name) {
        this._name = _name;
        return this;
    }

    public int getID() {
        return _ID;
    }

    public Group setID(int _ID) {
        this._ID = _ID;
        return this;
    }

    public ArrayList<String> getUsers() {
        return _users;
    }

    public Group setUsers(ArrayList<String> _users) {
        this._users = _users;
        return this;
    }

    @Override
    public String toString() {
        return _name;
    }
}
