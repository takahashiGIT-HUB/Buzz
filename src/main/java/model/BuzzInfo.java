package model;

import java.io.Serializable;

public record BuzzInfo(int buzzId,int postId,String userId) implements Serializable{

}
