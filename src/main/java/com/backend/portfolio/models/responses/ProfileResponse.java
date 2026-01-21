package com.backend.portfolio.models.responses;

import com.backend.portfolio.models.entities.Profile;
import com.backend.portfolio.models.states.enums.Sex;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {

    private Long id;
    private String fname;
    private String lname;
    private Sex sex;
    private String bio;
    private String banner;
    private String intro;
    private String contour;
    private String url;

    public static ProfileResponse fromProfile(Profile profile) {
        return ProfileResponse.builder()
                .id(profile.getId())
                .fname(profile.getFname())
                .lname(profile.getLname())
                .sex(profile.getSex())
                .bio(profile.getBio())
                .banner(profile.getBanner())
                .intro(profile.getIntro())
                .contour(profile.getContour())
                .url(profile.getUrl())
                .build();
    }
}

