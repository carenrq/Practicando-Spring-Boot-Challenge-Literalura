package com.aluracursos.literalura.services;

public interface IDataConverter
{
    <T> T RetrieveData(String json, Class<T> tClass);
}
