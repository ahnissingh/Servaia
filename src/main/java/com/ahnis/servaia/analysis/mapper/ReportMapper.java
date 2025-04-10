package com.ahnis.servaia.analysis.mapper;

import com.ahnis.servaia.analysis.dto.MoodReportApiResponse;
import com.ahnis.servaia.analysis.dto.MoodReportEmailResponse;
import com.ahnis.servaia.analysis.entity.MoodReportEntity;
import com.ahnis.servaia.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ReportMapper {
    @Mapping(target = "id", expression = "java(new org.bson.types.ObjectId().toHexString())")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "reportDate", expression = "java(java.time.Instant.now())") // Set reportDate to current time
    @Mapping(target = "moodSummary", source = "moodReport.moodSummary") // Map moodSummary
    @Mapping(target = "keyEmotions", source = "moodReport.keyEmotions") // Map keyEmotions
    @Mapping(target = "insights", source = "moodReport.insights") // Map insights
    @Mapping(target = "recommendations", source = "moodReport.recommendations") // Map recommendations
    @Mapping(target = "quote", source = "moodReport.quote")
    MoodReportEntity toMoodReportEntity(User user, MoodReportEmailResponse moodReport);

    @Mapping(target = "reportId", source = "id") // Map the entity's ID to reportId
    @Mapping(target = "reportDate", source = "reportDate")
    @Mapping(target = "moodSummary", source = "moodSummary")
    @Mapping(target = "keyEmotions", source = "keyEmotions")
    @Mapping(target = "insights", source = "insights")
    @Mapping(target = "recommendations", source = "recommendations")
    @Mapping(target = "quote", source = "quote")
    @Mapping(target = "createdAt", source = "createdAt")
    MoodReportApiResponse toApiResponse(MoodReportEntity entity);


}
