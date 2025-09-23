package ga.rdgtt.usager.controller;

import ga.rdgtt.usager.service.AirtelMoneySimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/airtel-money")
@CrossOrigin(origins = "*")
public class AirtelMoneyController {

    @Autowired
    private AirtelMoneySimulationService airtelMoneySimulationService;

    /**
     * Simuler un paiement Airtel Money
     */
    @PostMapping("/simulate-payment/{paymentId}")
    public ResponseEntity<Map<String, Object>> simulatePayment(
            @PathVariable UUID paymentId,
            @RequestBody Map<String, String> paymentData) {
        
        try {
            String phoneNumber = paymentData.get("phoneNumber");
            String pin = paymentData.get("pin");
            
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Numéro de téléphone requis"
                ));
            }
            
            if (pin == null || pin.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "PIN requis"
                ));
            }
            
            Map<String, Object> result = airtelMoneySimulationService.simulateAirtelMoneyPayment(
                paymentId, phoneNumber, pin
            );
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    /**
     * Obtenir les informations sur Airtel Money
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getAirtelMoneyInfo() {
        return ResponseEntity.ok(Map.of(
            "provider", "Airtel Money",
            "country", "Gabon",
            "currency", "XAF",
            "phone_format", "+241XXXXXXXX",
            "pin_format", "4 chiffres",
            "fees", "Variable selon le montant",
            "simulation_mode", true,
            "note", "Mode simulation activé - Aucun paiement réel ne sera effectué"
        ));
    }
}
