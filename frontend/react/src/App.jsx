import {Wrap,
        WrapItem,
        Spinner,
        Text} from "@chakra-ui/react";
import SidebarWithHeader from "./components/shared/SideBar.jsx"
import CardWithImage from "./components/Card.jsx";
import {useEffect,useState} from "react";
import {getCustomer} from "./services/client.js";

const App = () =>{
    const[isLoading, setIsLoading] = useState(false);
    const[customers,setCustomers] = useState([]);

    useEffect(() => {
        setIsLoading(true);
        getCustomer().then(res =>{
            setCustomers(res.data);
        }).catch(err =>{
            console.log(err)
        }).finally(() =>{
            setIsLoading(false);
        })
    }, []);

    if(isLoading)
        return (
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            </SidebarWithHeader>
        )
    if(customers.length <= 0)
        return (
            <SidebarWithHeader>
               <Text>No Customers found</Text>
            </SidebarWithHeader>
        )
    return(
        <SidebarWithHeader>
            <Wrap justify={"center"} spacing={"30px"}>
            {customers.map((customer,index) =>(
                <WrapItem key={index}>
                    <CardWithImage
                        {... customer}
                        imageNumber={index}/>
                </WrapItem>
            ))}
            </Wrap>
        </SidebarWithHeader>
    )
}

export default App