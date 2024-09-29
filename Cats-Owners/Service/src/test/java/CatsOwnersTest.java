import org.ISKor.dao.CatDAO;
import org.ISKor.dao.OwnerDAO;
import org.ISKor.dto.KittyDTO;
import org.ISKor.entity.Cat;
import org.ISKor.entity.Owner;
import org.ISKor.exception.CatServiceException;
import org.ISKor.exception.OwnerServiceException;
import org.ISKor.models.Breed;
import org.ISKor.models.Colour;
import org.ISKor.service.CatServiceImpl;
import org.ISKor.service.OwnerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class CatsOwnersTest {
    @Mock
    private CatDAO catDAO;

    @Mock
    private OwnerDAO ownerDAO;

    @InjectMocks
    private CatServiceImpl catService;

    @InjectMocks
    private OwnerServiceImpl ownerService;
    @BeforeEach
    void before() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOwner() throws CatServiceException, OwnerServiceException {
        doNothing().when(ownerDAO).addOwner(any(Owner.class));

        ownerService.createOwner("Ivan", LocalDate.of(2004, 8, 27));

        verify(ownerDAO).addOwner(any(Owner.class));
    }
    @Test
    void createCat() throws CatServiceException, OwnerServiceException {
        doNothing().when(catDAO).addCat(any(Cat.class));

        Owner owner = new Owner("Ivan", LocalDate.of(2004, 8, 27), new ArrayList<>());
        when(ownerDAO.getOwnerById(1)).thenReturn(owner);
        KittyDTO createdCat = catService.createCat("Kot", 1, "Burma", "White", LocalDate.of(2023, 12,1));

        verify(catDAO).addCat(any(Cat.class));
        verify(ownerDAO).getOwnerById(1);
    }

    @Test
    void createCatAndCheckConnectedToOwner() throws CatServiceException, OwnerServiceException {
        doNothing().when(catDAO).addCat(any(Cat.class));

        Owner owner = new Owner("Ivan", LocalDate.of(2004, 8, 27), new ArrayList<>());
        owner.setId(1);
        when(ownerDAO.getOwnerById(1)).thenReturn(owner);
        KittyDTO createdCat = catService.createCat("Kot", 1, "Burma", "White", LocalDate.of(2023, 12,1));

        verify(catDAO).addCat(any(Cat.class));
        verify(ownerDAO).getOwnerById(1);
        Assertions.assertEquals(1, owner.getCats().size());
        Assertions.assertEquals(owner.getCats().get(0).getId(), createdCat.getId());
        Assertions.assertEquals(owner.getId(), createdCat.getOwnerId());
    }

    @Test
    void addFriendsForCatAndCheckAllFriendsWasAdded() throws CatServiceException, OwnerServiceException {
        doNothing().when(catDAO).updateCat(any(Cat.class));

        Owner owner = new Owner("Ivan", LocalDate.of(2004, 8, 27), new ArrayList<>());
        Cat cat1 = new Cat("Kot", owner, Breed.Burma, Colour.Russet, LocalDate.of(2023, 8, 27), new ArrayList<>());
        Cat cat2 = new Cat("Kot", owner, Breed.Burma, Colour.Russet, LocalDate.of(2023, 8, 27), new ArrayList<>());
        Cat cat3 = new Cat("Kot", owner, Breed.Burma, Colour.Russet, LocalDate.of(2023, 8, 27), new ArrayList<>());
        cat1.setId(1);
        cat2.setId(2);
        cat3.setId(3);

        when(catDAO.getCatById(cat1.getId())).thenReturn(cat1);
        when(catDAO.getCatById(cat2.getId())).thenReturn(cat2);
        when(catDAO.getCatById(cat3.getId())).thenReturn(cat3);

        catService.makeFriends(cat1.getId(), cat2.getId());
        catService.makeFriends(cat1.getId(), cat3.getId());

        Assertions.assertEquals(cat2.getFriends().size(), 1);
        Assertions.assertEquals(cat3.getFriends().size(), 1);
        Assertions.assertEquals(cat1.getFriends().size(), 2);

        Assertions.assertEquals(cat2.getFriends().get(0).getId(), cat1.getId());
        Assertions.assertEquals(cat3.getFriends().get(0).getId(), cat1.getId());
        Assertions.assertEquals(cat1.getFriends().get(0).getId(), cat2.getId());
        Assertions.assertEquals(cat1.getFriends().get(1).getId(), cat3.getId());
    }

    @Test
    void unfriendFriendsForCatAndCheckAllFriendsWasAdded() throws CatServiceException, OwnerServiceException {
        doNothing().when(catDAO).updateCat(any(Cat.class));

        Owner owner = new Owner("Ivan", LocalDate.of(2003, 10, 15), new ArrayList<>());
        Cat cat1 = new Cat("Kot", owner, Breed.Burma, Colour.Russet, LocalDate.of(2023, 8, 27), new ArrayList<>());
        Cat cat2 = new Cat("Kot", owner, Breed.Burma, Colour.Russet, LocalDate.of(2023, 8, 27), new ArrayList<>());
        cat1.setId(1);
        cat2.setId(2);

        when(catDAO.getCatById(cat1.getId())).thenReturn(cat1);
        when(catDAO.getCatById(cat2.getId())).thenReturn(cat2);

        catService.makeFriends(cat1.getId(), cat2.getId());

        Assertions.assertEquals( 1, cat1.getFriends().size());
        Assertions.assertEquals(1, cat1.getFriends().size());

        Assertions.assertEquals(cat1.getFriends().get(0).getId(), cat2.getId());
        Assertions.assertEquals(cat2.getFriends().get(0).getId(), cat1.getId());

        catService.unfriend(cat1.getId(), cat2.getId());

        Assertions.assertEquals(0, cat1.getFriends().size());
        Assertions.assertEquals(0, cat2.getFriends().size());
    }

    @Test
    void addOwnerForCatAndOwnerShouldAdd() throws CatServiceException, OwnerServiceException {
        doNothing().when(catDAO).updateCat(any(Cat.class));
        doNothing().when(ownerDAO).updateOwner(any(Owner.class));

        Owner owner1 = new Owner("Ivan", LocalDate.of(2003, 10, 15), new ArrayList<>());
        Owner owner2 = new Owner("Nikita", LocalDate.of(2004, 5, 5), new ArrayList<>());
        owner1.setId(1);
        owner2.setId(2);
        Cat cat1 = new Cat("Kot", owner1, Breed.Burma, Colour.Russet, LocalDate.of(2023, 8, 27), new ArrayList<>());
        cat1.setId(1);


        when(catDAO.getCatById(cat1.getId())).thenReturn(cat1);
        when(ownerDAO.getOwnerById(owner1.getId())).thenReturn(owner1);
        when(ownerDAO.getOwnerById(owner2.getId())).thenReturn(owner2);

        ownerService.addCat(owner2.getId(), cat1.getId());

        Assertions.assertEquals(1, owner2.getCats().size());
        Assertions.assertEquals(cat1.getId(), owner2.getCats().get(0).getId());
        Assertions.assertEquals(owner2, cat1.getOwner());
    }
}
