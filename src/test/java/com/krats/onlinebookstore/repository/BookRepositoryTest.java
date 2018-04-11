package com.krats.onlinebookstore.repository;

import com.krats.onlinebookstore.bean.IsbnGenerator;
import com.krats.onlinebookstore.bean.NumberGenerator;
import com.krats.onlinebookstore.bean.TextUtil;
import com.krats.onlinebookstore.model.Book;
import com.krats.onlinebookstore.model.Language;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class BookRepositoryTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(BookRepository.class)
                .addClass(Book.class)
                .addClass(Language.class)
                .addClass(TextUtil.class)
                .addClass(NumberGenerator.class)
                .addClass(IsbnGenerator.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml");
    }

    @Inject
    private BookRepository bookRepository;

    @Test
    public void create() {

        assert(bookRepository.countAll()).equals(0);
        assertEquals(0, bookRepository.findAll().size());
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        Book book = new Book(
                "a title",
                null,
                100.0,
                "hgjh",
                yesterday,
                100,
                null,
                Language.ENGLISH);
        book = bookRepository.create(book);

        assertEquals(1, bookRepository.findAll().size());
        assert(bookRepository.countAll()).equals(1);

        assertNotNull(book.getId());

        Book bookFound = bookRepository.find(book.getId());

        assertEquals("a title", bookFound.getTitle());

        bookRepository.delete(book.getId());

        assert(bookRepository.countAll()).equals(0);
        assertEquals(0, bookRepository.findAll().size());

    }

    @Test(expected = Exception.class)
    public void createInvalidBook() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        Book book = new Book(
                null,
                null,
                100.0,
                "hgjh",
                yesterday,
                100,
                null,
                Language.ENGLISH);
        bookRepository.create(book);
    }

    @Test
    public void checkBookSanitization() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        Book book = new Book(
                "a     title",
                null,
                100.0,
                "hgjh",
                yesterday,
                100,
                null,
                Language.ENGLISH);
        book = bookRepository.create(book);
        assertEquals("a title", bookRepository.find(book.getId()).getTitle());
        assertTrue(book.getIsbn().startsWith("13"));

        bookRepository.delete(book.getId());

        assert(bookRepository.countAll()).equals(0);
        assertEquals(0, bookRepository.findAll().size());
    }

    @Test(expected = Exception.class)
    public void findWithInvalidId() {
        bookRepository.find(null);
    }
}
