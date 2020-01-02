package ru.zagamaza.sublearn.subtitles.client.opensubtitles;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString()
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubtitleInfo {

    @JsonProperty("MatchedBy")
    private String matchedBy;
    @JsonProperty("IDSubMovieFile")
    private String iDSubMovieFile;
    @JsonProperty("MovieHash")
    private String movieHash;
    @JsonProperty("MovieByteSize")
    private String movieByteSize;
    @JsonProperty("MovieTimeMS")
    private String movieTimeMS;
    @JsonProperty("IDSubtitleFile")
    private String iDSubtitleFile;
    @JsonProperty("SubFileName")
    private String subFileName;
    @JsonProperty("SubActualCD")
    private String subActualCD;
    @JsonProperty("SubSize")
    private String subSize;
    @JsonProperty("SubHash")
    private String subHash;

    @JsonProperty("SubLastTS")
    private String subLastTS;

    @JsonProperty("SubTSGroup")
    private String subTSGroup;
    @JsonProperty("InfoReleaseGroup")
    private String infoReleaseGroup;
    @JsonProperty("InfoFormat")
    private String infoFormat;
    @JsonProperty("InfoOther")
    private String infoOther;
    @JsonProperty("IDSubtitle")
    private String iDSubtitle;
    @JsonProperty("UserID")
    private String userID;
    @JsonProperty("SubLanguageID")
    private String subLanguageID;
    @JsonProperty("SubFormat")
    private String subFormat;
    @JsonProperty("SubSumCD")
    private String subSumCD;
    @JsonProperty("SubAuthorComment")
    private String subAuthorComment;
    @JsonProperty("SubAddDate")
    private String subAddDate;
    @JsonProperty("SubBad")
    private String subBad;
    @JsonProperty("SubRating")
    private String subRating;
    @JsonProperty("SubSumVotes")
    private String subSumVotes;
    @JsonProperty("SubDownloadsCnt")
    private String subDownloadsCnt;
    @JsonProperty("MovieReleaseName")
    private String movieReleaseName;
    @JsonProperty("MovieFPS")
    private String movieFPS;
    @JsonProperty("IDMovie")
    private String iDMovie;
    @JsonProperty("IDMovieImdb")
    private String iDMovieImdb;
    @JsonProperty("MovieName")
    private String movieName;
    @JsonProperty("MovieNameEng")
    private Object movieNameEng;
    @JsonProperty("MovieYear")
    private String movieYear;
    @JsonProperty("MovieImdbRating")
    private String movieImdbRating;
    @JsonProperty("SubFeatured")
    private String subFeatured;
    @JsonProperty("UserNickName")
    private String userNickName;
    @JsonProperty("SubTranslator")
    private String subTranslator;
    @JsonProperty("ISO639")
    private String iSO639;
    @JsonProperty("LanguageName")
    private String languageName;
    @JsonProperty("SubComments")
    private String subComments;
    @JsonProperty("SubHearingImpaired")
    private String subHearingImpaired;
    @JsonProperty("UserRank")
    private String userRank;
    @JsonProperty("SeriesSeason")
    private String seriesSeason;
    @JsonProperty("SeriesEpisode")
    private String seriesEpisode;
    @JsonProperty("MovieKind")
    private String movieKind;
    @JsonProperty("SubHD")
    private String subHD;
    @JsonProperty("SeriesIMDBParent")
    private String seriesIMDBParent;
    @JsonProperty("SubEncoding")
    private String subEncoding;
    @JsonProperty("SubAutoTranslation")
    private String subAutoTranslation;
    @JsonProperty("SubForeignPartsOnly")
    private String subForeignPartsOnly;
    @JsonProperty("SubFromTrusted")
    private String subFromTrusted;
    @JsonProperty("QueryCached")
    private Integer queryCached;
    @JsonProperty("SubTSGroupHash")
    private String subTSGroupHash;
    @JsonProperty("SubDownloadLink")
    private String subDownloadLink;
    @JsonProperty("ZipDownloadLink")
    private String zipDownloadLink;
    @JsonProperty("SubtitlesLink")
    private String subtitlesLink;
    @JsonProperty("QueryNumber")
    private String queryNumber;
    @JsonProperty("Score")
    private Double score;
}