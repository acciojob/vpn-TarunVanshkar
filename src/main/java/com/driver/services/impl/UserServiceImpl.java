package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService
{

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception
    {
        if(username==null || password==null || countryName==null)
        {
            throw new Exception();
        }
        CountryName countryName1 = CountryName.valueOf(countryName);

        Country country = new Country();
        country.setCountryName(countryName1);
        country.setCode(countryName1.toCode());

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setOriginalIp(country.getCode()+"."+user.getId());
        user.setMaskedIp(null);
        user.setConnected(false);

        country.setUser(user);
        countryRepository3.save(country);  // child will be automatically saved
        return user;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId)
    {
        //subscribe to the serviceProvider by adding it to the list of providers and return updated User
        User user = userRepository3.findById(userId).get();
        ServiceProvider serviceProvider = serviceProviderRepository3.findById(serviceProviderId).get();

        List<ServiceProvider> serviceProviderList = user.getServiceProviderList();
        serviceProviderList.add(serviceProvider);
        user.setServiceProviderList(serviceProviderList);
        userRepository3.save(user);
        return user;
    }
}
