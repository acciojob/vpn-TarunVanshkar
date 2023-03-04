package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception
    {
        User user = userRepository2.findById(userId).get();
        if(user.getConnected())
        {
            throw new Exception("Already connected");
        }
        else if(user.getOriginalCountry().getCountryName().name().equals(countryName))
        {
            return user;
        }

        List<ServiceProvider> serviceProviderList = user.getServiceProviderList();
        for(ServiceProvider serviceProvider : serviceProviderList)
        {
            List<Country> countryList = serviceProvider.getCountryList();
            for(Country country : countryList)
            {
                String currCountry = country.getCountryName().name();
                if(currCountry.equals(countryName))
                {
                    user.setConnected(true);
                    CountryName countryName1 = CountryName.valueOf(countryName);
                    user.setMaskedIp(countryName1.toCode()+"."+serviceProvider.getId()+"."+userId);
                    userRepository2.save(user);
                    return user;
                }
            }

        }

        throw new Exception("Unable to connect");
    }
    @Override
    public User disconnect(int userId) throws Exception
    {
        //If the given user was not connected to a vpn, throw "Already disconnected" exception.
        //Else, disconnect from vpn, make masked Ip as null, update relevant attributes and return updated user.
        User user = userRepository2.findById(userId).get();
        if(!user.getConnected())
        {
            throw new Exception("Already disconnected");
        }

        user.setConnected(false);
        user.setMaskedIp(null);
        userRepository2.save(user);
        return user;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception
    {

        try
        {
            User sender = userRepository2.findById(senderId).get();
            User receiver = userRepository2.findById(receiverId).get();
            if(sender.getOriginalCountry() != receiver.getOriginalCountry())
            {
                String country = sender.getOriginalCountry().getCountryName().name();
                connect(senderId, country);
            }
            return sender;
        }
        catch (Exception e)
        {
            throw new Exception("Cannot establish communication");
        }

    }
}
