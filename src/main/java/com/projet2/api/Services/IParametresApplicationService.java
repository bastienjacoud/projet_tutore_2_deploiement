package com.projet2.api.Services;

import com.projet2.api.Entities.ParametresapplicationEntity;

public interface IParametresApplicationService {
    ParametresapplicationEntity findFirst() throws Exception;
    ParametresapplicationEntity save(ParametresapplicationEntity parametres);
    boolean choicesAvailable() throws Exception;
    boolean schedulesAvailable() throws Exception;
}
