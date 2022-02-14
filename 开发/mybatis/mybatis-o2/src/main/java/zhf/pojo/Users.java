package zhf.pojo;

/**
 * @author zhf
 * @date 2022/1/5
 */
public class Users {
    private int id;
    private String name;
    private String pawd;
    public Users() {
    }
    public Users(int id, String name, String pawd) {
        this.id = id;
        this.name = name;
        this.pawd = pawd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPawd() {
        return pawd;
    }

    public void setPawd(String pawd) {
        this.pawd = pawd;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pawd='" + pawd + '\'' +
                '}';
    }
}
