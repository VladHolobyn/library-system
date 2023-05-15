package com.pnudev.librarysystem.mapper;

import com.pnudev.librarysystem.dto.BorrowingDTO;
import com.pnudev.librarysystem.entity.Borrowing;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, BookMapper.class})
public interface BorrowingMapper {

    BorrowingDTO toDTO(Borrowing borrowing);
}
