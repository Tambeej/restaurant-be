package com.rest_au_rant.service;

import com.google.zxing.WriterException;
import com.rest_au_rant.exception.ResourceNotFoundException;
import com.rest_au_rant.exception.UnauthorizedException;
import com.rest_au_rant.model.Restaurant;
import com.rest_au_rant.model.RestaurantTable;
import com.rest_au_rant.repository.RestaurantRepository;
import com.rest_au_rant.repository.TableRepository;
import com.rest_au_rant.utils.PermissionUtil;
import com.rest_au_rant.utils.QRGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TableService {
    private final TableRepository tableRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public TableService(TableRepository tableRepository, RestaurantRepository restaurantRepository) {
        this.tableRepository = tableRepository;
        this.restaurantRepository = restaurantRepository;
    }
    public RestaurantTable addTable(Long restaurantId) throws UnauthorizedException, IOException, WriterException {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        Integer maxTableNumber = tableRepository.findMaxTableNumberByRestaurantId(restaurantId);
        int newTableNumber = (maxTableNumber != null ? maxTableNumber : 0) + 1;

        RestaurantTable table = new RestaurantTable();
        table.setRestaurant(restaurant);
        table.setTableNumber(newTableNumber);
        tableRepository.save(table);

        String qrPath = setQRCode(table.getId());
        table.setQRCodePath(qrPath);

        return tableRepository.save(table);
    }

    public String setQRCode(Long tableId) throws IOException, WriterException {
        tableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Error: table with ID " + tableId + " not found."));

        return QRGenerator.generateQRCode(tableId);
    }


    public List<RestaurantTable> getAllTables() {
        return tableRepository.findAll();
    }

    public List<RestaurantTable> getTablesByRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Error: restaurant with ID " + restaurantId + " not found."));

        PermissionUtil.checkRestaurantPermission(restaurant);

        return tableRepository.findByRestaurant(restaurant);
    }



    public void deleteTable(Long restaurantId) {
        Integer maxNumber = tableRepository.findMaxTableNumberByRestaurantId(restaurantId);
        if (maxNumber == null) {
            throw new ResourceNotFoundException("No tables to delete");
        }

        RestaurantTable lastTable = tableRepository
                .findByRestaurantIdAndTableNumber(restaurantId, maxNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));

        tableRepository.delete(lastTable);
    }

}
