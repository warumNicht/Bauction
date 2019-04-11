package bauction.services.contracts;

import bauction.domain.models.serviceModels.TownServiceModel;

public interface TownService {
    TownServiceModel findByNameOrElseCreateByName(String name);
}
