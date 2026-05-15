package com.oaosis.samak.domain.analysis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LocationInfo {

    @Column(name = "location_raw_text")
    private String rawText;

    @Column(name = "location_lat")
    private Double lat;

    @Column(name = "location_lng")
    private Double lng;

    @Column(name = "location_admin_level")
    private String adminLevel;

    @Column(name = "location_zoom")
    private Integer zoom;

    @Column(name = "location_status")
    private String locationStatus;

    @Column(name = "location_viewport_ne_lat")
    private Double viewportNeLat;

    @Column(name = "location_viewport_ne_lng")
    private Double viewportNeLng;

    @Column(name = "location_viewport_sw_lat")
    private Double viewportSwLat;

    @Column(name = "location_viewport_sw_lng")
    private Double viewportSwLng;
}
