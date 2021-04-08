package com.pieta.weatherapp

import org.junit.Test

import org.junit.Assert.*

class ResponseParserUT {
    private val test = "{\"lat\":33.4418,\"lon\":-94.0377,\"timezone\":\"America/Chicago\",\"timezone_offset\":-18000,\"hourly\":[{\"dt\":1617807600,\"temp\":292.6,\"feels_like\":292.9,\"pressure\":1010,\"humidity\":88,\"dew_point\":290.56,\"uvi\":0.37,\"clouds\":90,\"visibility\":10000,\"wind_speed\":6.02,\"wind_deg\":179,\"wind_gust\":13.41,\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"pop\":0.27},{\"dt\":1617811200,\"temp\":292.74,\"feels_like\":293.03,\"pressure\":1010,\"humidity\":87,\"dew_point\":290.52,\"uvi\":0.5,\"clouds\":95,\"visibility\":10000,\"wind_speed\":6.3,\"wind_deg\":181,\"wind_gust\":13.44,\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"pop\":0.46},{\"dt\":1617814800,\"temp\":292.31,\"feels_like\":292.66,\"pressure\":1009,\"humidity\":91,\"dew_point\":290.81,\"uvi\":0.68,\"clouds\":98,\"visibility\":10000,\"wind_speed\":6.34,\"wind_deg\":190,\"wind_gust\":13.3,\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"pop\":0.72},{\"dt\":1617818400,\"temp\":292.13,\"feels_like\":292.54,\"pressure\":1008,\"humidity\":94,\"dew_point\":291.14,\"uvi\":0.78,\"clouds\":99,\"visibility\":10000,\"wind_speed\":5.71,\"wind_deg\":196,\"wind_gust\":12.89,\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"pop\":0.8},{\"dt\":1617822000,\"temp\":291.92,\"feels_like\":292.36,\"pressure\":1007,\"humidity\":96,\"dew_point\":291.27,\"uvi\":4.51,\"clouds\":100,\"visibility\":6383,\"wind_speed\":5.74,\"wind_deg\":206,\"wind_gust\":12.93,\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"pop\":0.99,\"rain\":{\"1h\":2.35}},{\"dt\":1617825600,\"temp\":289.48,\"feels_like\":289.62,\"pressure\":1007,\"humidity\":94,\"dew_point\":288.55,\"uvi\":3.69,\"clouds\":100,\"visibility\":10000,\"wind_speed\":6.03,\"wind_deg\":276,\"wind_gust\":12.91,\"weather\":[{\"id\":502,\"main\":\"Rain\",\"description\":\"heavy intensity rain\",\"icon\":\"10d\"}],\"pop\":0.99,\"rain\":{\"1h\":6.49}},{\"dt\":1617829200,\"temp\":292.12,\"feels_like\":292.11,\"pressure\":1006,\"humidity\":78,\"dew_point\":288.34,\"uvi\":2.49,\"clouds\":86,\"visibility\":10000,\"wind_speed\":6.09,\"wind_deg\":256,\"wind_gust\":11.4,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"pop\":0.96,\"rain\":{\"1h\":0.52}},{\"dt\":1617832800,\"temp\":294.32,\"feels_like\":294.06,\"pressure\":1006,\"humidity\":60,\"dew_point\":286.32,\"uvi\":2.3,\"clouds\":65,\"visibility\":10000,\"wind_speed\":6.65,\"wind_deg\":285,\"wind_gust\":11.76,\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"pop\":0.75},{\"dt\":1617836400,\"temp\":293.21,\"feels_like\":292.71,\"pressure\":1006,\"humidity\":55,\"dew_point\":284.1,\"uvi\":0.83,\"clouds\":52,\"visibility\":10000,\"wind_speed\":5.48,\"wind_deg\":274,\"wind_gust\":11.03,\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"pop\":0.75},{\"dt\":1617840000,\"temp\":290.79,\"feels_like\":290.25,\"pressure\":1008,\"humidity\":63,\"dew_point\":283.86,\"uvi\":0.17,\"clouds\":43,\"visibility\":10000,\"wind_speed\":2.99,\"wind_deg\":273,\"wind_gust\":6.06,\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"pop\":0.75},{\"dt\":1617843600,\"temp\":288.37,\"feels_like\":287.67,\"pressure\":1008,\"humidity\":66,\"dew_point\":282.22,\"uvi\":0,\"clouds\":0,\"visibility\":10000,\"wind_speed\":2.45,\"wind_deg\":262,\"wind_gust\":2.95,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"pop\":0},{\"dt\":1617847200,\"temp\":287.45,\"feels_like\":286.76,\"pressure\":1010,\"humidity\":70,\"dew_point\":282.21,\"uvi\":0,\"clouds\":0,\"visibility\":10000,\"wind_speed\":2.76,\"wind_deg\":266,\"wind_gust\":3.85,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"pop\":0},{\"dt\":1617850800,\"temp\":286.69,\"feels_like\":285.98,\"pressure\":1010,\"humidity\":72,\"dew_point\":281.9,\"uvi\":0,\"clouds\":0,\"visibility\":10000,\"wind_speed\":2.18,\"wind_deg\":252,\"wind_gust\":2.23,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"pop\":0},{\"dt\":1617854400,\"temp\":286.17,\"feels_like\":285.41,\"pressure\":1010,\"humidity\":72,\"dew_point\":281.35,\"uvi\":0,\"clouds\":0,\"visibility\":10000,\"wind_speed\":2.21,\"wind_deg\":238,\"wind_gust\":2.22,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"pop\":0},{\"dt\":1617858000,\"temp\":285.63,\"feels_like\":284.84,\"pressure\":1010,\"humidity\":73,\"dew_point\":280.99,\"uvi\":0,\"clouds\":0,\"visibility\":10000,\"wind_speed\":2.06,\"wind_deg\":234,\"wind_gust\":2.15,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"pop\":0},{\"dt\":1617861600,\"temp\":285.06,\"feels_like\":284.26,\"pressure\":1009,\"humidity\":75,\"dew_point\":280.88,\"uvi\":0,\"clouds\":0,\"visibility\":10000,\"wind_speed\":2.3,\"wind_deg\":240,\"wind_gust\":2.36,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"pop\":0},{\"dt\":1617865200,\"temp\":284.55,\"feels_like\":283.76,\"pressure\":1009,\"humidity\":77,\"dew_point\":280.83,\"uvi\":0,\"clouds\":0,\"visibility\":10000,\"wind_speed\":2.27,\"wind_deg\":233,\"wind_gust\":2.41,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"pop\":0},{\"dt\":1617868800,\"temp\":284.13,\"feels_like\":283.37,\"pressure\":1008,\"humidity\":80,\"dew_point\":280.93,\"uvi\":0,\"clouds\":0,\"visibility\":10000,\"wind_speed\":2.61,\"wind_deg\":246,\"wind_gust\":4.34,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"pop\":0},{\"dt\":1617872400,\"temp\":283.72,\"feels_like\":282.97,\"pressure\":1009,\"humidity\":82,\"dew_point\":280.95,\"uvi\":0,\"clouds\":0,\"visibility\":10000,\"wind_speed\":2.86,\"wind_deg\":262,\"wind_gust\":6.95,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"pop\":0},{\"dt\":1617876000,\"temp\":283.39,\"feels_like\":282.64,\"pressure\":1009,\"humidity\":83,\"dew_point\":280.75,\"uvi\":0,\"clouds\":0,\"visibility\":10000,\"wind_speed\":2.95,\"wind_deg\":267,\"wind_gust\":8.12,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"pop\":0},{\"dt\":1617879600,\"temp\":283.04,\"feels_like\":281.51,\"pressure\":1009,\"humidity\":83,\"dew_point\":280.4,\"uvi\":0,\"clouds\":0,\"visibility\":10000,\"wind_speed\":3.04,\"wind_deg\":260,\"wind_gust\":8.77,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"pop\":0},{\"dt\":1617883200,\"temp\":282.94,\"feels_like\":281.26,\"pressure\":1010,\"humidity\":81,\"dew_point\":280.03,\"uvi\":0,\"clouds\":0,\"visibility\":10000,\"wind_speed\":3.26,\"wind_deg\":244,\"wind_gust\":9.21,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"pop\":0},{\"dt\":1617886800,\"temp\":284.54,\"feels_like\":283.77,\"pressure\":1010,\"humidity\":78,\"dew_point\":280.87,\"uvi\":0.37,\"clouds\":0,\"visibility\":10000,\"wind_speed\":3.73,\"wind_deg\":243,\"wind_gust\":11.32,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"pop\":0},{\"dt\":1617890400,\"temp\":287.73,\"feels_like\":287.02,\"pressure\":1010,\"humidity\":68,\"dew_point\":282.02,\"uvi\":1.36,\"clouds\":0,\"visibility\":10000,\"wind_speed\":4.36,\"wind_deg\":253,\"wind_gust\":11.34,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"pop\":0},{\"dt\":1617894000,\"temp\":290.43,\"feels_like\":289.75,\"pressure\":1010,\"humidity\":59,\"dew_point\":282.45,\"uvi\":3.16,\"clouds\":0,\"visibility\":10000,\"wind_speed\":5.08,\"wind_deg\":258,\"wind_gust\":10.27,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"pop\":0},{\"dt\":1617897600,\"temp\":292.7,\"feels_like\":292.04,\"pressure\":1010,\"humidity\":51,\"dew_point\":282.55,\"uvi\":5.36,\"clouds\":3,\"visibility\":10000,\"wind_speed\":5.5,\"wind_deg\":260,\"wind_gust\":10.09,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"pop\":0},{\"dt\":1617901200,\"temp\":294.34,\"feels_like\":293.74,\"pressure\":1010,\"humidity\":47,\"dew_point\":282.68,\"uvi\":7.3,\"clouds\":2,\"visibility\":10000,\"wind_speed\":5.88,\"wind_deg\":260,\"wind_gust\":9.43,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"pop\":0},{\"dt\":1617904800,\"temp\":295.63,\"feels_like\":295.08,\"pressure\":1009,\"humidity\":44,\"dew_point\":283.09,\"uvi\":8.32,\"clouds\":2,\"visibility\":10000,\"wind_speed\":5.89,\"wind_deg\":260,\"wind_gust\":9.14,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"pop\":0},{\"dt\":1617908400,\"temp\":296.51,\"feels_like\":296.02,\"pressure\":1009,\"humidity\":43,\"dew_point\":283.53,\"uvi\":7.97,\"clouds\":2,\"visibility\":10000,\"wind_speed\":5.82,\"wind_deg\":260,\"wind_gust\":8.73,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"pop\":0},{\"dt\":1617912000,\"temp\":297.17,\"feels_like\":296.75,\"pressure\":1008,\"humidity\":43,\"dew_point\":284.24,\"uvi\":6.53,\"clouds\":5,\"visibility\":10000,\"wind_speed\":5.24,\"wind_deg\":259,\"wind_gust\":8.1,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"pop\":0},{\"dt\":1617915600,\"temp\":297.52,\"feels_like\":297.21,\"pressure\":1007,\"humidity\":46,\"dew_point\":285.38,\"uvi\":4.41,\"clouds\":6,\"visibility\":10000,\"wind_speed\":4.48,\"wind_deg\":259,\"wind_gust\":7.62,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"pop\":0},{\"dt\":1617919200,\"temp\":297.38,\"feels_like\":297.14,\"pressure\":1007,\"humidity\":49,\"dew_point\":286.36,\"uvi\":2.33,\"clouds\":6,\"visibility\":10000,\"wind_speed\":3.92,\"wind_deg\":257,\"wind_gust\":6.91,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"pop\":0},{\"dt\":1617922800,\"temp\":296.25,\"feels_like\":296.1,\"pressure\":1007,\"humidity\":57,\"dew_point\":287.51,\"uvi\":0.85,\"clouds\":22,\"visibility\":10000,\"wind_speed\":2.93,\"wind_deg\":256,\"wind_gust\":6.19,\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02d\"}],\"pop\":0},{\"dt\":1617926400,\"temp\":293.57,\"feels_like\":293.39,\"pressure\":1007,\"humidity\":66,\"dew_point\":287.17,\"uvi\":0.18,\"clouds\":33,\"visibility\":10000,\"wind_speed\":2.03,\"wind_deg\":252,\"wind_gust\":2.55,\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"pop\":0},{\"dt\":1617930000,\"temp\":291.28,\"feels_like\":290.92,\"pressure\":1007,\"humidity\":68,\"dew_point\":285.53,\"uvi\":0,\"clouds\":42,\"visibility\":10000,\"wind_speed\":1.97,\"wind_deg\":213,\"wind_gust\":2.07,\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03n\"}],\"pop\":0},{\"dt\":1617933600,\"temp\":290.36,\"feels_like\":290.02,\"pressure\":1007,\"humidity\":72,\"dew_point\":285.47,\"uvi\":0,\"clouds\":69,\"visibility\":10000,\"wind_speed\":2.09,\"wind_deg\":192,\"wind_gust\":2.14,\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04n\"}],\"pop\":0},{\"dt\":1617937200,\"temp\":289.66,\"feels_like\":289.32,\"pressure\":1007,\"humidity\":75,\"dew_point\":285.32,\"uvi\":0,\"clouds\":74,\"visibility\":10000,\"wind_speed\":1.8,\"wind_deg\":193,\"wind_gust\":1.84,\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04n\"}],\"pop\":0.05},{\"dt\":1617940800,\"temp\":288.91,\"feels_like\":288.55,\"pressure\":1007,\"humidity\":77,\"dew_point\":285.1,\"uvi\":0,\"clouds\":76,\"visibility\":10000,\"wind_speed\":1.89,\"wind_deg\":193,\"wind_gust\":1.95,\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04n\"}],\"pop\":0.05},{\"dt\":1617944400,\"temp\":288.55,\"feels_like\":288.18,\"pressure\":1007,\"humidity\":78,\"dew_point\":285,\"uvi\":0,\"clouds\":80,\"visibility\":10000,\"wind_speed\":1.7,\"wind_deg\":184,\"wind_gust\":1.73,\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04n\"}],\"pop\":0.17},{\"dt\":1617948000,\"temp\":288.24,\"feels_like\":287.89,\"pressure\":1007,\"humidity\":80,\"dew_point\":284.93,\"uvi\":0,\"clouds\":72,\"visibility\":10000,\"wind_speed\":2.67,\"wind_deg\":181,\"wind_gust\":3.37,\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04n\"}],\"pop\":0.21},{\"dt\":1617951600,\"temp\":287.76,\"feels_like\":287.5,\"pressure\":1006,\"humidity\":85,\"dew_point\":285.35,\"uvi\":0,\"clouds\":0,\"visibility\":10000,\"wind_speed\":3.83,\"wind_deg\":168,\"wind_gust\":9.15,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"pop\":0.27},{\"dt\":1617955200,\"temp\":287.46,\"feels_like\":287.32,\"pressure\":1006,\"humidity\":91,\"dew_point\":286.11,\"uvi\":0,\"clouds\":0,\"visibility\":10000,\"wind_speed\":3.68,\"wind_deg\":168,\"wind_gust\":10.47,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"pop\":0.32},{\"dt\":1617958800,\"temp\":288,\"feels_like\":287.86,\"pressure\":1006,\"humidity\":89,\"dew_point\":286.28,\"uvi\":0,\"clouds\":1,\"visibility\":10000,\"wind_speed\":4.65,\"wind_deg\":173,\"wind_gust\":13.23,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"pop\":0.25},{\"dt\":1617962400,\"temp\":288.15,\"feels_like\":287.95,\"pressure\":1006,\"humidity\":86,\"dew_point\":285.93,\"uvi\":0,\"clouds\":1,\"visibility\":10000,\"wind_speed\":4.62,\"wind_deg\":179,\"wind_gust\":13.43,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"pop\":0.25},{\"dt\":1617966000,\"temp\":287.33,\"feels_like\":287.18,\"pressure\":1005,\"humidity\":91,\"dew_point\":286.08,\"uvi\":0,\"clouds\":4,\"visibility\":10000,\"wind_speed\":3.05,\"wind_deg\":169,\"wind_gust\":9.41,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"pop\":0.25},{\"dt\":1617969600,\"temp\":287.05,\"feels_like\":286.98,\"pressure\":1005,\"humidity\":95,\"dew_point\":286.43,\"uvi\":0,\"clouds\":5,\"visibility\":10000,\"wind_speed\":3.14,\"wind_deg\":163,\"wind_gust\":10.6,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"pop\":0.31},{\"dt\":1617973200,\"temp\":288.74,\"feels_like\":288.81,\"pressure\":1007,\"humidity\":94,\"dew_point\":288.04,\"uvi\":0.33,\"clouds\":74,\"visibility\":10000,\"wind_speed\":1.33,\"wind_deg\":203,\"wind_gust\":6.48,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"pop\":0.82,\"rain\":{\"1h\":0.34}},{\"dt\":1617976800,\"temp\":291.14,\"feels_like\":291.34,\"pressure\":1007,\"humidity\":90,\"dew_point\":289.65,\"uvi\":1.19,\"clouds\":82,\"visibility\":10000,\"wind_speed\":1.84,\"wind_deg\":184,\"wind_gust\":5.33,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"pop\":0.77,\"rain\":{\"1h\":0.15}}],\"daily\":[{\"dt\":1617818400,\"sunrise\":1617796519,\"sunset\":1617842436,\"temp\":{\"day\":292.13,\"min\":286.17,\"max\":294.32,\"night\":286.17,\"eve\":290.79,\"morn\":291.77},\"feels_like\":{\"day\":292.54,\"night\":291.91,\"eve\":290.25,\"morn\":291.91},\"pressure\":1008,\"humidity\":94,\"dew_point\":291.14,\"wind_speed\":5.71,\"wind_deg\":196,\"weather\":[{\"id\":502,\"main\":\"Rain\",\"description\":\"heavy intensity rain\",\"icon\":\"10d\"}],\"clouds\":99,\"pop\":0.99,\"rain\":9.51,\"uvi\":4.51},{\"dt\":1617904800,\"sunrise\":1617882842,\"sunset\":1617928880,\"temp\":{\"day\":295.63,\"min\":282.94,\"max\":297.52,\"night\":288.91,\"eve\":293.57,\"morn\":282.94},\"feels_like\":{\"day\":295.08,\"night\":281.26,\"eve\":293.39,\"morn\":281.26},\"pressure\":1009,\"humidity\":44,\"dew_point\":283.09,\"wind_speed\":5.89,\"wind_deg\":260,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":2,\"pop\":0.05,\"uvi\":8.32},{\"dt\":1617991200,\"sunrise\":1617969165,\"sunset\":1618015324,\"temp\":{\"day\":293.41,\"min\":287.05,\"max\":297.35,\"night\":289.74,\"eve\":294.54,\"morn\":287.05},\"feels_like\":{\"day\":293.89,\"night\":286.98,\"eve\":295.01,\"morn\":286.98},\"pressure\":1005,\"humidity\":92,\"dew_point\":292.18,\"wind_speed\":1.64,\"wind_deg\":112,\"weather\":[{\"id\":502,\"main\":\"Rain\",\"description\":\"heavy intensity rain\",\"icon\":\"10d\"}],\"clouds\":94,\"pop\":1,\"rain\":41.05,\"uvi\":3.77},{\"dt\":1618077600,\"sunrise\":1618055488,\"sunset\":1618101768,\"temp\":{\"day\":293.96,\"min\":285.48,\"max\":294.9,\"night\":285.48,\"eve\":290.37,\"morn\":287.88},\"feels_like\":{\"day\":293.25,\"night\":287.86,\"eve\":289.82,\"morn\":287.86},\"pressure\":1008,\"humidity\":44,\"dew_point\":281.64,\"wind_speed\":4.68,\"wind_deg\":334,\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"clouds\":5,\"pop\":1,\"rain\":1.5,\"uvi\":7.94},{\"dt\":1618164000,\"sunrise\":1618141812,\"sunset\":1618188213,\"temp\":{\"day\":294.55,\"min\":281.97,\"max\":296.95,\"night\":288.77,\"eve\":293.2,\"morn\":281.97},\"feels_like\":{\"day\":293.97,\"night\":281.22,\"eve\":292.96,\"morn\":281.22},\"pressure\":1009,\"humidity\":47,\"dew_point\":283,\"wind_speed\":2.3,\"wind_deg\":133,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":0,\"pop\":0,\"uvi\":8},{\"dt\":1618250400,\"sunrise\":1618228137,\"sunset\":1618274657,\"temp\":{\"day\":298.17,\"min\":285.7,\"max\":300.68,\"night\":288.55,\"eve\":296.55,\"morn\":285.7},\"feels_like\":{\"day\":298.35,\"night\":285.39,\"eve\":296.88,\"morn\":285.39},\"pressure\":1011,\"humidity\":62,\"dew_point\":290.54,\"wind_speed\":1.64,\"wind_deg\":224,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":65,\"pop\":0.65,\"rain\":0.76,\"uvi\":8},{\"dt\":1618336800,\"sunrise\":1618314462,\"sunset\":1618361101,\"temp\":{\"day\":287.01,\"min\":284.68,\"max\":289.11,\"night\":287.45,\"eve\":288.01,\"morn\":284.93},\"feels_like\":{\"day\":286.12,\"night\":284.51,\"eve\":287.41,\"morn\":284.51},\"pressure\":1020,\"humidity\":64,\"dew_point\":280.41,\"wind_speed\":4.83,\"wind_deg\":50,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":100,\"pop\":0.97,\"rain\":3.97,\"uvi\":8},{\"dt\":1618423200,\"sunrise\":1618400788,\"sunset\":1618447546,\"temp\":{\"day\":287.77,\"min\":284.22,\"max\":287.77,\"night\":285.81,\"eve\":286.54,\"morn\":284.23},\"feels_like\":{\"day\":286.88,\"night\":283.56,\"eve\":285.87,\"morn\":283.56},\"pressure\":1021,\"humidity\":61,\"dew_point\":280.39,\"wind_speed\":4.79,\"wind_deg\":57,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":100,\"pop\":0.42,\"rain\":0.92,\"uvi\":8}]}"
    private val parser = ResponseParser()

    @Test
    fun parse_empty() {
        parser.parse("")
        assertNull(parser.hourly)
        assertNull(parser.daily)
    }

    @Test
    fun parse_corrupted() {
        parser.parse("{\"lat\":33.4418,\"lon\":-94.0377,\"timezone\":\"America/Chicago\",\"timezone_offset\":-18000,\"hourly\":[{\"dt\":16178")
        assertNull(parser.hourly)
        assertNull(parser.daily)
    }

    @Test
    fun parse_hourly_values() {
        parser.parse(test)
        val hourlyArray = parser.hourly
        val last = if(hourlyArray != null) hourlyArray.size - 1 else 0

        assertEquals(1617807600, hourlyArray?.get(0)?.dt)
        assertEquals(292.6f, hourlyArray?.get(0)?.temp)
        assertEquals(292.9f, hourlyArray?.get(0)?.feels_like)
        assertEquals(88, hourlyArray?.get(0)?.humidity)
        assertEquals(6.02f, hourlyArray?.get(0)?.wind_speed)
        assertEquals(179, hourlyArray?.get(0)?.wind_deg)
        assertEquals(0.27f, hourlyArray?.get(0)?.pop)

        assertEquals(1617976800, hourlyArray?.get(last)?.dt)
        assertEquals(291.14f, hourlyArray?.get(last)?.temp)
        assertEquals(291.34f, hourlyArray?.get(last)?.feels_like)
        assertEquals(90, hourlyArray?.get(last)?.humidity)
        assertEquals(1.84f, hourlyArray?.get(last)?.wind_speed)
        assertEquals(184, hourlyArray?.get(last)?.wind_deg)
        assertEquals(0.77f, hourlyArray?.get(last)?.pop)
    }

    @Test
    fun parse_hourly_weather() {
        parser.parse(test)
        val hourlyArray = parser.hourly
        val last = if(hourlyArray != null) hourlyArray.size - 1 else 0

        assertEquals(804, hourlyArray?.get(0)?.weather?.get(0)?.id)
        assertEquals("Clouds", hourlyArray?.get(0)?.weather?.get(0)?.main)
        assertEquals("overcast clouds", hourlyArray?.get(0)?.weather?.get(0)?.description)
        assertEquals("04d", hourlyArray?.get(0)?.weather?.get(0)?.icon)

        assertEquals(500, hourlyArray?.get(last)?.weather?.get(0)?.id)
        assertEquals("Rain", hourlyArray?.get(last)?.weather?.get(0)?.main)
        assertEquals("light rain", hourlyArray?.get(last)?.weather?.get(0)?.description)
        assertEquals("10d", hourlyArray?.get(last)?.weather?.get(0)?.icon)
    }

    @Test
    fun parse_daily_values() {
        parser.parse(test)
        val dailyArray = parser.daily
        val last = if(dailyArray != null) dailyArray.size - 1 else 0

        assertEquals(1617818400, dailyArray?.get(0)?.dt)
        assertEquals(0.99f, dailyArray?.get(0)?.pop)

        assertEquals(1618423200, dailyArray?.get(last)?.dt)
        assertEquals(0.42f, dailyArray?.get(last)?.pop)
    }

    @Test
    fun parse_daily_temp() {
        parser.parse(test)
        val dailyArray = parser.daily
        val last = if(dailyArray != null) dailyArray.size - 1 else 0

        assertEquals(292.13f, dailyArray?.get(0)?.temp?.day)

        assertEquals(287.77f, dailyArray?.get(last)?.temp?.day)
    }

    @Test
    fun parse_daily_weather() {
        parser.parse(test)
        val dailyArray = parser.daily
        val last = if(dailyArray != null) dailyArray.size - 1 else 0

        assertEquals(502, dailyArray?.get(0)?.weather?.get(0)?.id)
        assertEquals("Rain", dailyArray?.get(0)?.weather?.get(0)?.main)
        assertEquals("heavy intensity rain", dailyArray?.get(0)?.weather?.get(0)?.description)
        assertEquals("10d", dailyArray?.get(0)?.weather?.get(0)?.icon)

        assertEquals(500, dailyArray?.get(last)?.weather?.get(0)?.id)
        assertEquals("Rain", dailyArray?.get(last)?.weather?.get(0)?.main)
        assertEquals("light rain", dailyArray?.get(last)?.weather?.get(0)?.description)
        assertEquals("10d", dailyArray?.get(last)?.weather?.get(0)?.icon)
    }
}