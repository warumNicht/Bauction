package beginfunc.services;

import beginfunc.domain.entities.productRelated.Town;
import beginfunc.domain.models.serviceModels.TownServiceModel;
import beginfunc.repositories.TownRepository;
import beginfunc.services.contracts.TownService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TownServiceImpl implements TownService {
    private final TownRepository townRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TownServiceImpl(TownRepository townRepository, ModelMapper modelMapper) {
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TownServiceModel findByNameOrElseCreateByName(String name) {
        Town town = this.townRepository.findByName(name).orElse(null);
        if(town!=null){
            return this.modelMapper.map(town,TownServiceModel.class);
        }
        return this.modelMapper.map(this.townRepository.saveAndFlush(new Town(name)),TownServiceModel.class);
    }
}
