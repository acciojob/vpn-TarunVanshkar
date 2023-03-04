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
        if(countryName.equals("aus")|| countryName.equals("usa") || countryName.equals("ind") ||
                countryName.equals("jpn") || countryName.equals("chi"))
        {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);

            Country country = new Country();

            if (countryName.equals("aus"))
            {
                country.setCountryName(CountryName.AUS);
                country.setCode(CountryName.AUS.toCode());
            }
            if (countryName.equals("usa"))
            {
                country.setCountryName(CountryName.USA);
                country.setCode(CountryName.USA.toCode());
            }
            if (countryName.equals("ind"))
            {
                country.setCountryName(CountryName.IND);
                country.setCode(CountryName.IND.toCode());
            }
            if (countryName.equals("jpn"))
            {
                country.setCountryName(CountryName.JPN);
                country.setCode(CountryName.JPN.toCode());
            }
            if (countryName.equals("chi"))
            {
                country.setCountryName(CountryName.CHI);
                country.setCode(CountryName.CHI .toCode());
            }

            user.setOriginalCountry(country);
            user.setConnected(false);

            String originalIp = country.getCode()+"."+user.getId();
            user.setOriginalIp(originalIp);
            country.setUser(user);

            userRepository3.save(user);
            return user;
        }
        else
        {
            throw new Exception();
        }
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
        serviceProvider.getUsers().add(user);

        serviceProviderRepository3.save(serviceProvider);
        return user;
    }
}
