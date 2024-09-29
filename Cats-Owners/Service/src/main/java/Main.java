import org.ISKor.dao.CatDAOImpl;
import org.ISKor.dao.OwnerDAOImpl;
import org.ISKor.dto.KittyDTO;
import org.ISKor.dto.OwnerDTO;
import org.ISKor.service.CatServiceImpl;
import org.ISKor.service.OwnerServiceImpl;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        CatServiceImpl catServiceImpl = new CatServiceImpl(new CatDAOImpl(), new OwnerDAOImpl());
        OwnerServiceImpl ownerServiceImpl = new OwnerServiceImpl(new CatDAOImpl(), new OwnerDAOImpl());

        OwnerDTO owner = ownerServiceImpl.createOwner("Owner", LocalDate.of(2003, 10, 15));
        KittyDTO firstCat = catServiceImpl.createCat("Cat", owner.getId(), "Burma", "White", LocalDate.of(2020, 1,1));
        KittyDTO secondCat = catServiceImpl.createCat("Cat", owner.getId(), "Burma", "Grey", LocalDate.of(2020, 1,1));
        catServiceImpl.makeFriends(firstCat.getId(), secondCat.getId());
        catServiceImpl.unfriend(firstCat.getId(), secondCat.getId());
        catServiceImpl.deleteCat(firstCat.getId());
        ownerServiceImpl.deleteOwner(owner.getId());
    }
}