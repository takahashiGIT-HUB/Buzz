package model;

import java.io.Serializable;
import java.util.List;

public record RankingInfo(String shopName ,List<PostInfo> comment,int postId)implements Serializable {

}
