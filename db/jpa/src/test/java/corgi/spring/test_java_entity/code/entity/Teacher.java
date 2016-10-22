package corgi.spring.test_java_entity.code.entity;

import com.dounine.corgi.jpa.entity.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

/**
 * Created by lgq on 16-10-14.
 */
@Entity
@Table(name="test_teacher")
public class Teacher extends BaseEntity {
    private char sex;//性别
    private String name; //姓名
    private Integer age;//年龄
    private String diploma;//文凭

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDiploma() {
        return diploma;
    }

    public void setDiploma(String diploma) {
        this.diploma = diploma;
    }


}
