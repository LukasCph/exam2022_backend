package facades;

import DTO.WashingAssistantDTO.WashingAssistantDTO;
import com.google.gson.Gson;
import entities.WashingAssistant;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.lang.annotation.Native;
import java.util.List;

public class AssistantFacade {

    private static AssistantFacade instance;
    private static EntityManagerFactory emf;
    private Gson gson = new Gson();

    //Private Constructor to ensure Singleton
    private AssistantFacade() {}


    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static AssistantFacade getAssistantFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AssistantFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public List<WashingAssistantDTO> getAllAssistants(){
        EntityManager em = emf.createEntityManager();
        List<WashingAssistant> allAssistants;
        try{
            TypedQuery<WashingAssistant> tq = em.createQuery("SELECT w FROM WashingAssistant w",WashingAssistant.class);
            allAssistants = tq.getResultList();
        }finally{
            em.close();
        }
        return WashingAssistantDTO.getDTO(allAssistants);
    }

    public WashingAssistantDTO newAssistant(WashingAssistantDTO washingAssistantDTO) {
        EntityManager em = emf.createEntityManager();
        WashingAssistant assistant = null;
        try {
            em.getTransaction().begin();
            assistant = new WashingAssistant(washingAssistantDTO.getWasherName(),
                                            washingAssistantDTO.getLanguage(),
                                            washingAssistantDTO.getYearsOfExp(),
                                            washingAssistantDTO.getPriceHour()

                    );
            em.persist(assistant);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new WashingAssistantDTO(assistant);
    }

    public WashingAssistantDTO removeWashingAssistant(String assistantName) {
        EntityManager em = emf.createEntityManager();
        WashingAssistant assistantToRemove;

        try {
            em.getTransaction().begin();
            assistantToRemove = em.find(WashingAssistant.class,assistantName);
            em.remove(assistantToRemove);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new WashingAssistantDTO(assistantToRemove);
    }
}
