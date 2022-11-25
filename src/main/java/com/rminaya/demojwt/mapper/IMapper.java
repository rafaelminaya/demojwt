package com.rminaya.demojwt.mapper;

public interface IMapper<I, O> {
    O map(I in);
}
