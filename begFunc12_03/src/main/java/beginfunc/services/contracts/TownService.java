package beginfunc.services.contracts;

import beginfunc.domain.models.serviceModels.TownServiceModel;

public interface TownService {
    TownServiceModel findByNameOrElseCreateByName(String name);
}
