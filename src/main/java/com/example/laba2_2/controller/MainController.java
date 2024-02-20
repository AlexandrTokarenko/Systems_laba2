package com.example.laba2_2.controller;

import com.example.laba2_2.db.BookDB;
import com.example.laba2_2.db.BorrowingDB;
import com.example.laba2_2.db.ReaderDB;
import com.example.laba2_2.entity.Borrowing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.sql.Date;

@Controller
public class MainController {

    @Autowired
    private BookDB bookDB;

    @Autowired
    private ReaderDB readerDB;

    @Autowired
    private BorrowingDB borrowingDB;

    @Autowired
    @Qualifier("postgresqlDataSource")
    private DataSource postgresql;

    @Autowired
    @Qualifier("mysqlDataSource")
    private DataSource mysql;

    private DataSource currentDataSource;
    private String currentDataSourceStr;


    @GetMapping()
    public String index() {
        return "data-source";
    }


    @PostMapping("/select-db")
    public String selectDatabase(@RequestParam("dbType") String dbType, Model model) {
    /*    if ("postgresql".equals(dbType)) {
            model.addAttribute("dataSource", postgresqlDataSource);
        } else if ("mysql".equals(dbType)) {
            model.addAttribute("dataSource", mysqlDataSource);
        }*/

        currentDataSource = fromStringToDataSource(dbType);
        currentDataSourceStr = dbType;
        model.addAttribute("readers",readerDB.getAll(currentDataSource));
        return "readers";
    }



  /*  @GetMapping
    public String index(Model model) {

        model.addAttribute("readers",readerDB.getAll());
        return "readers";
    }*/

    @GetMapping("/borrowed-books/{readerId}")
    public String showBorrowedBooks(@PathVariable int readerId, Model model) {

        model.addAttribute("borrowed_books",bookDB.getAllByReaderId(readerId,currentDataSource));
        model.addAttribute("readerId", readerId);

        return "borrowed-books";
    }

    @GetMapping("/return-book/{readerId}/{bookId}")
    public String returnBook(@PathVariable int bookId, @PathVariable int readerId, Model model) {

//        borrowingDB.deleteByBookIdAndReaderId(bookId,readerId);

        borrowingDB.updateBorrowingDateReturned(bookId,readerId, new Date(System.currentTimeMillis()),currentDataSource);
        return "redirect:/borrowed-books/" + readerId;

    }

    @GetMapping("/delete-reader/{readerId}")
    public String deleteReader(@PathVariable int readerId, Model model) {

        borrowingDB.deleteEntryAndReader(readerId,currentDataSource,getAnotherDataSource(currentDataSourceStr));

        return "redirect:/";
    }

    @GetMapping("/borrow-book/{readerId}")
    public String borrowBook(@PathVariable int readerId, Model model){

        model.addAttribute("books",bookDB.getAll(currentDataSource));
        model.addAttribute("readerId",readerId);

        return "index";
    }

    @GetMapping("/borrow-book/{readerId}/{bookId}")
    public String borrowBookByReaderAndBook(@PathVariable int bookId, @PathVariable int readerId, Model model) {

        borrowingDB.save(new Borrowing(bookId,readerId,new Date(System.currentTimeMillis()),null),currentDataSource);

        return "redirect:/";
    }

    private DataSource fromStringToDataSource(String dataSource) {

        if ("postgresql".equals(dataSource)) {
           return postgresql;
        } else if ("mysql".equals(dataSource)) {
            return mysql;
        }

        return null;
    }

    private DataSource getAnotherDataSource(String dataSource) {

        if ("postgresql".equals(dataSource)) {
            return mysql;
        } else if ("mysql".equals(dataSource)) {
            return postgresql;
        }

        return null;
    }
}
