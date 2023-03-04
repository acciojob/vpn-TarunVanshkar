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
        if(user.getMaskedIp()!=null)
        {
            throw new Exception("Already connected");
        }
        else if(countryName.equals(user.getOriginalCountry().getCountryName().toString()))
        {
            return user;
        }
        else
        {
            if(user.getServiceProviderList()==null)
            {
                throw new Exception("Unable to connect");
            }

            List<ServiceProvider> serviceProviderList = user.getServiceProviderList();
            int minId = Integer.MAX_VALUE;
            ServiceProvider serviceProvider = null;
            Country country = null;
            for(ServiceProvider serviceProvider1 : serviceProviderList)
            {
                List<Country> countryList = serviceProvider1.getCountryList();
                for(Country country1 : countryList)
                {
                    if(countryName.equals(country1.getCountryName().toString()) && serviceProvider1.getId()<minId)
                    {
                        minId = serviceProvider1.getId();
                        serviceProvider=serviceProvider1;
                        country=country1;
                    }
                }
            }

            if(serviceProvider!=null)
            {
                Connection connection = new Connection();
                connection.setUser(user);
                connection.setServiceProvider(serviceProvider);

                String countryCode = country.getCode();
                int providerId = serviceProvider.getId();
                String maskedId = countryCode+"."+providerId+"."+userId;

                user.setMaskedIp(maskedId);
                user.setConnected(true);
                user.getConnectionList().add(connection);

                serviceProvider.getConnectionList().add(connection);

                userRepository2.save(user);
                serviceProviderRepository2.save(serviceProvider);
                return user;
            }
            else
            {
                throw new Exception("Unable to connect");
            }
        }
    }
    @Override
    public User disconnect(int userId) throws Exception
    {
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
        User sender = userRepository2.findById(senderId).get();
        User receiver = userRepository2.findById(receiverId).get();

        if(receiver.getMaskedIp()!=null)
        {
            String countryCode = receiver.getMaskedIp().substring(0,3);

            if(countryCode.equals(sender.getOriginalCountry().getCode()))
            {
                return sender;
            }
            else
            {
                String countryName="";

                if(countryCode.equals(CountryName.AUS.toCode()))
                {
                    countryName=CountryName.AUS.toString();
                }
                if(countryCode.equals(CountryName.USA.toCode()))
                {
                    countryName=CountryName.USA.toString();
                }
                if(countryCode.equals(CountryName.JPN.toCode()))
                {
                    countryName=CountryName.JPN.toString();
                }
                if(countryCode.equals(CountryName.IND.toCode()))
                {
                    countryName=CountryName.IND.toString();
                }
                if(countryCode.equals(CountryName.CHI.toCode()))
                {
                    countryName=CountryName.CHI.toString();
                }

                try
                {
                    User newSender = connect(senderId, countryName);
                    return newSender;
                }
                catch(Exception e)
                {
                    throw new Exception("Cannot establish communication");
                }
            }
        }
        else
        {
            if(receiver.getOriginalCountry().equals(sender.getOriginalCountry()))
            {
                return sender;
            }
            else
            {
                String countryName = receiver.getOriginalCountry().getCountryName().toString();
                try
                {
                    User newSender = connect(senderId, countryName);
                    return newSender;
                }
                catch(Exception e)
                {
                    throw new Exception("Cannot establish communication");
                }
            }
        }
    }
}
